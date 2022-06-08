package com.beyt.reflection;

import com.beyt.reflection.annotation.MyFieldAutowired;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MyFieldAutowiredInjector {
    private final ApplicationContext applicationContext;

    @PostConstruct
    private void customAutowired() {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            if (beanName.equalsIgnoreCase(this.getClass().getSimpleName())) {
                continue;
            }

            Object bean = applicationContext.getBean(beanName);
            /*
             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
             * Proxy use java.lang.reflect.Proxy#isProxyClass
             */
            Class<?> objClz = bean.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(bean)) {
                objClz = org.springframework.aop.support.AopUtils.getTargetClass(bean);
            }

            try {
                for (Field injecteeField : objClz.getDeclaredFields()) {
                    if (injecteeField.isAnnotationPresent(MyFieldAutowired.class)) {

                        // Set Operation
                        boolean accessible = injecteeField.canAccess(bean);

                        injecteeField.setAccessible(true);

                        injecteeField.set(bean, "New Value injected " + UUID.randomUUID());

                        injecteeField.setAccessible(accessible);
                    }
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        }
    }
}
