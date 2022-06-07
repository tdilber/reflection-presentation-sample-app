package com.beyt.reflection;

import com.beyt.reflection.annotation.MyFieldAutowired;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Field;
import java.util.UUID;

@Lazy
@Configuration
public class MyFieldAutowiredInjector implements ApplicationContextAware {

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        customAutowired(applicationContext);
    }

    private void customAutowired(ApplicationContext applicationContext) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
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
                        boolean accessible = injecteeField.isAccessible();
                        // boolean accessible = injecteeField.canAccess(bean);
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
