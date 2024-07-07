package com.example.demo_plugin_aop.config;

import com.example.demo_plugin_aop.extension.ExtensionManager;
import com.example.demo_plugin_aop.interceptor.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ExtensionManager extensionManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new RequestInterceptor(extensionManager));
    }
}
