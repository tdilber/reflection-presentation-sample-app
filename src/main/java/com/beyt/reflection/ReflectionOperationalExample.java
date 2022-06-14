package com.beyt.reflection;

import com.beyt.reflection.annotation.MyFieldAutowired;
import com.beyt.reflection.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Profile("operation")
public class ReflectionOperationalExample {

    private static String injectStaticField = "Default Start Static Value";

    private String injectField = "Default Start Value";

    @MyFieldAutowired
    private String injectedWithAutowiredField = "Default Start Value";

    @EventListener(classes = {ContextRefreshedEvent.class, ContextStartedEvent.class, ContextStoppedEvent.class})
    public void handleMultipleEvents(ApplicationContextEvent applicationContextEvent) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        ReflectionOperationalExample bean = applicationContext.getBean(ReflectionOperationalExample.class);

        fieldInjectionExample(applicationContextEvent, bean);

        methodInvokeExample(bean);
    }

    @SuppressWarnings("unchecked")
    private void methodInvokeExample(ReflectionOperationalExample bean) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ReflectionOperationalExample.class.getDeclaredMethod("invokingMethod", Long.class, String.class, UserDTO.class);
        List<String> invoke = (List<String>) method.invoke(bean, 50L, "Test Str " + randomValue(), new UserDTO(null, "Name " + randomValue(), null));

        log.info("Invoke Result => {}", String.join(", ", invoke));
        log.info("------------------------------------------------");
    }

    private String randomValue() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    private static List<String> invokingMethod(Long val, String s, UserDTO userDTO) {
        List<String> result = new ArrayList<>();
        result.add(val + "");
        result.add(s);
        result.add(userDTO.getName());

        return result;
    }

    private void fieldInjectionExample(ApplicationContextEvent applicationContextEvent, ReflectionOperationalExample bean) throws IllegalAccessException {
        printInjectField(applicationContextEvent.getClass().getSimpleName());

        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);

        Field[] declaredFields = targetClass.getDeclaredFields();

        Field injecteeField = Arrays.stream(declaredFields).filter(f -> f.getName().equals("injectField")).findFirst().orElseThrow(IllegalStateException::new);

        // Set Operation
        boolean accessible = injecteeField.canAccess(bean);

        injecteeField.setAccessible(true);

        injecteeField.set(bean, "New Value injected " + UUID.randomUUID());

        injecteeField.setAccessible(accessible);

        Field injecteeStaticField = Arrays.stream(declaredFields).filter(f -> f.getName().equals("injectStaticField")).findFirst().orElseThrow(IllegalStateException::new);

        injecteeStaticField.set(null, "New Static Value injected " + UUID.randomUUID());
    }

    private void printInjectField(String eventName) {
        log.info("------------------------------------------------");
        log.info("Injected Field New Value : {} Event name : {}", injectField, eventName);
        log.info("Injected Static Field New Value : {} Event name : {}", injectStaticField, eventName);
        log.info("Injected Field With Autowired New Value : {} Event name : {}", injectedWithAutowiredField, eventName);
        log.info("------------------------------------------------");
    }


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public ReflectionOperationalExample(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
