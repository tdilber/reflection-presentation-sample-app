package com.beyt.reflection;

import com.beyt.reflection.service.IUserService;
import com.beyt.reflection.service.MetricsTestService;
import com.beyt.reflection.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
@Profile("proxy")
public class ProxyExample implements ApplicationRunner {

    private final MetricsTestService metricsTestService;

    public ProxyExample(MetricsTestService metricsTestService) {
        this.metricsTestService = metricsTestService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        IUserService interfaceProxy = createProxy();
        log.info("--------------- Spring Proxy ------------------");
        proxyTest(metricsTestService);
        log.info("---------------- Interface Proxy -----------------");
        proxyTest(interfaceProxy);
        log.info("--------------------------------------------------");
        interfaceProxy.getUserById(1L);
        interfaceProxy.getUserList();
    }

    protected void proxyTest(Object object) {
        log.info(object.getClass().getName());
        log.info("Is Proxy Class : {}", Proxy.isProxyClass(object.getClass()));
        log.info("isAopProxy : {}", AopUtils.isAopProxy(object));
        log.info("AopProxyUtils Proxy Resolver Class Result : {}", AopProxyUtils.ultimateTargetClass(object));
        log.info("My Proxy Resolver Class Result : {}", getTargetClass(object));
        log.info("Jhipster Proxy Resolver Class Result : {}", getTargetClassLikeJhipster(object));
    }

    protected IUserService createProxy() {
        return (IUserService) Proxy.newProxyInstance(
                ProxyExample.class.getClassLoader(),
                new Class[]{IUserService.class},
                new TimeBasedInvocationHandler(new UserService()));
    }

    public static class TimeBasedInvocationHandler<T> implements InvocationHandler {
        private T object;

        public TimeBasedInvocationHandler(T object) {
            this.object = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log.info("Method Name : {} invoked at : {} nano", method.getName(), System.nanoTime());
            Object result = method.invoke(object, args);
            log.info("Method Name : {} invoked end at : {} nano result : {}", method.getName(), System.nanoTime(), result.toString());
            return result;
        }
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
