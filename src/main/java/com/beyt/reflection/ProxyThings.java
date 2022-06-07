package com.beyt.reflection;

import com.beyt.reflection.service.MetricsTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
public class ProxyThings implements ApplicationRunner {

    private final MetricsTestService metricsTestService;

    public ProxyThings(MetricsTestService metricsTestService) {
        this.metricsTestService = metricsTestService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //proxyTest();
    }

    protected void proxyTest() {
        log.info(metricsTestService.getClass().getName());
        log.info("Is Proxy Class : {}", Proxy.isProxyClass(metricsTestService.getClass()));
        log.info("isAopProxy : {}", AopUtils.isAopProxy(metricsTestService));
        log.info("AopProxyUtils Proxy Resolver Class Result : {}", AopProxyUtils.ultimateTargetClass(metricsTestService));
        log.info("My Proxy Resolver Class Result : {}", getTargetClass(metricsTestService));
        log.info("Jhipster Proxy Resolver Class Result : {}", getTargetClassLikeJhipster(metricsTestService));
    }

    public static Class<?> getTargetClass(Object object) {
        Class<?> clazz = object.getClass();

        if (Proxy.isProxyClass(clazz)) {
            clazz = clazz.getInterfaces()[0];
        }

        return (AopUtils.isCglibProxy(object) ? clazz.getSuperclass() : clazz);
    }

    public static Class<?> getTargetClassLikeJhipster(Object obj) {
        Class<?> objClz = obj.getClass();
        if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
            objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
        }
        return objClz;
    }


    /**
     * JHipster PreAuthorized
     *
     * @param applicationContext
     */
    public void jhipsterCodeBlock(ApplicationContext applicationContext) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object obj = applicationContext.getBean(beanName);
            /*
             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
             * Proxy use java.lang.reflect.Proxy#isProxyClass
             */
            Class<?> objClz = obj.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
                objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
            }

            for (Method m : objClz.getDeclaredMethods()) {
//                if (m.isAnnotationPresent(PreAuthorize.class)) {
//                    addMethodToMap(m);
//                }
            }
        }
    }
}
