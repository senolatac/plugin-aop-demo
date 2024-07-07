package com.example.demo_plugin_aop.interceptor;

import com.example.demo_plugin_aop.annotation.Extendable;
import com.example.demo_plugin_aop.extension.ExtensionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

//https://medium.com/@aedemirsen/what-is-spring-boot-request-interceptor-and-how-to-use-it-7fd85f3df7f7

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {
    private final ExtensionManager extensionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        log.debug("1 - pre handle.");
        log.debug("METHOD type:" + request.getMethod());
        log.debug("Request URI: " + request.getRequestURI());
        log.debug("Servlet PATH: " + request.getServletPath());
        //check which controller method is requested
        if(object instanceof HandlerMethod){
            //can be added different logics
            Class<?> controllerClass = ((HandlerMethod) object).getBeanType();
            String methodName = ((HandlerMethod) object).getMethod().getName();
            log.info("Controller name: " + controllerClass.getName());
            log.info("Method name:" + methodName);
            return modifyRequest(request, response, (HandlerMethod) object);
        }
        return true;
    }

    private boolean modifyRequest(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        Extendable extendable = handlerMethod.getMethod().getAnnotation(Extendable.class);
        if (extendable == null) {
            return true;
        }
        Object extensionBean = extensionManager.getBeanByExtensionId(extendable.id());
        if (extensionBean == null) {
            return true;
        }
        ExtensionRequestWrapperHandler extensionRequestWrapperHandler = new ExtensionRequestWrapperHandler(request, extensionBean);
        return HandlerInterceptor.super.preHandle(extensionRequestWrapperHandler, response, handlerMethod);
    }
}
