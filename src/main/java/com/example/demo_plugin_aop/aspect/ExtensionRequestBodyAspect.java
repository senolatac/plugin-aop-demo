package com.example.demo_plugin_aop.aspect;

import com.example.demo_plugin_aop.annotation.Extendable;
import com.example.demo_plugin_aop.annotation.PreModifyRequestProcess;
import com.example.demo_plugin_aop.extension.ExtensionManager;
import com.example.demo_plugin_aop.utils.GsonUtils;
import com.example.demo_plugin_aop.utils.MethodUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

//https://www.baeldung.com/spring-boot-change-request-body-before-controller

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExtensionRequestBodyAspect implements RequestBodyAdvice {
    private final ExtensionManager extensionManager;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.info("Target-Request-Body-Type: {}", targetType);
        return methodParameter.hasMethodAnnotation(Extendable.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        Extendable extendable = parameter.getMethodAnnotation(Extendable.class);
        Object extensionBean = extensionManager.getBeanByExtensionId(extendable.id());
        if (extensionBean == null) {
            return inputMessage;
        }
        return callExtensionPreProcess(inputMessage, extensionBean);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private HttpInputMessage callExtensionPreProcess(HttpInputMessage inputMessage, Object extensionBean) throws IOException {
        //TODO: validation for list-size=1 or 0
        List<Method> preProcessList = MethodUtils.getMethodsAnnotatedWith(extensionBean.getClass(), PreModifyRequestProcess.class);
        if (!CollectionUtils.isEmpty(preProcessList)) {
            Method preMethod = preProcessList.get(0);
            try {
                //TODO: make generic
                Object response = preMethod.invoke(extensionBean, GsonUtils.fromInputStream(inputMessage.getBody(), preMethod.getParameterTypes()[preMethod.getParameterTypes().length - 1]));
                return inputMessageResult(inputMessage, response);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return inputMessage;
    }

    private HttpInputMessage inputMessageResult(HttpInputMessage inputMessage, Object response) {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return GsonUtils.toInputStream(response);
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }
}
