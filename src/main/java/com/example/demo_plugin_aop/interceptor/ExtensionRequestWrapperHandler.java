package com.example.demo_plugin_aop.interceptor;

import com.example.demo_plugin_aop.annotation.PreModifyRequestProcess;
import com.example.demo_plugin_aop.utils.GsonUtils;
import com.example.demo_plugin_aop.utils.MethodUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExtensionRequestWrapperHandler extends HttpServletRequestWrapper {
    private final Object extensionBean;
    private final HttpServletRequest originalRequest;
    private final String body;

    public ExtensionRequestWrapperHandler(HttpServletRequest request, Object extensionBean) throws IOException {
        super(request);
        this.originalRequest = request;
        this.extensionBean = extensionBean;
        this.body = this.inputStreamToString(this.callExtensionPreProcess());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final byte[] myBytes = body.getBytes(StandardCharsets.UTF_8);
        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return (lastIndexRetrieved == myBytes.length-1);
            }

            @Override
            public boolean isReady() {
                // This implementation will never block
                // We also never need to call the readListener from this method, as this method will never return false
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = myBytes[lastIndexRetrieved+1];
                    lastIndexRetrieved++;
                    if (isFinished() && (readListener != null)) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    private InputStream callExtensionPreProcess() throws IOException {
        //TODO: validation for list-size=1 or 0
        List<Method> preProcessList = MethodUtils.getMethodsAnnotatedWith(extensionBean.getClass(), PreModifyRequestProcess.class);
        if (!CollectionUtils.isEmpty(preProcessList)) {
            Method preMethod = preProcessList.get(0);
            try {
                //TODO: make generic
                Object response = preMethod.invoke(extensionBean, GsonUtils.fromInputStream(originalRequest.getInputStream(), preMethod.getParameterTypes()[preMethod.getParameterTypes().length - 1]));
                return GsonUtils.toInputStream(response);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return originalRequest.getInputStream();
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }
}
