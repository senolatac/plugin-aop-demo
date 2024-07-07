package com.example.demo_plugin_aop.extension;

import com.example.demo_plugin_aop.annotation.Extension;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExtensionManager {
    private final ApplicationContext applicationContext;

    private Map<String, Object> extendedIdBeanMap;

    @PostConstruct
    public void init() {
        this.extendedIdBeanMap = findExtendedBeans();
    }

    public Object getBeanByExtensionId(String extensionId) {
        return getExtendedBeans().get(extensionId);
    }

    public Map<String, Object> getExtendedBeans() {
        //use it with memory cache
        if (extendedIdBeanMap == null) {
            extendedIdBeanMap = findExtendedBeans();
        }
        return extendedIdBeanMap;
    }

    private Map<String, Object> findExtendedBeans() {
        Map<String, Object> idBeanMap = new HashMap<>();
        Map<String, Object> allBeansWithNames = applicationContext.getBeansWithAnnotation(Extension.class);
        //If you want the annotated data
        allBeansWithNames.forEach((beanName, bean) -> {
            Extension extensionAnnotation = applicationContext.findAnnotationOnBean(beanName, Extension.class);
            log.debug("extensionAnnotation: {}", extensionAnnotation.id());

            //TODO: Multiple same-id problem, consider with @Order
            idBeanMap.put(extensionAnnotation.id(), bean);
            //List<Method> preProcesses = MethodUtils.getMethodsAnnotatedWith(bean.getClass(), PreProcess.class);
            //log.info("preProcess methods size: {}", preProcesses.size());
        });
        return idBeanMap;
    }
}
