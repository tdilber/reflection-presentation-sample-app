package com.beyt.reflection;

import com.beyt.reflection.controller.UserController;
import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.service.UserService;
import com.beyt.reflection.util.GenericTypeResolverUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReflectionFullClassPrintExample implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printSomeClasses();
    }

    private void printSomeClasses() {
        System.out.println("---------------------");
        printClass(UserController.class);
        System.out.println("---------------------");
        printClass(UserService.class);
        System.out.println("---------------------");
        printClass(UserDTO.class);
        System.out.println("---------------------");
        printClass(ProxyThings.class);
        System.out.println("---------------------");
        printClass(ReflectionFullClassPrintExample.class);
    }

    private static void printClass(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(clazz.getPackage().getName()).append(";\n\n\n")
                .append(Arrays.stream(clazz.getAnnotations()).map(a -> "@" + a.annotationType().getSimpleName() + "\n").collect(Collectors.joining()))
                .append(Modifier.toString(clazz.getModifiers())).append(" class ").append(clazz.getSimpleName())
                .append(" {\n\n");

        Stream.of(clazz.getDeclaredFields()).forEach(field ->
                sb.append("\t").append(Arrays.stream(field.getAnnotations()).map(a -> "@" + a.annotationType().getSimpleName() + "\n").collect(Collectors.joining()))
                        .append("\t").append(Modifier.toString(field.getModifiers())).append(" ").append(GenericTypeResolverUtil.resolveFieldGenericsTree(field).printResult())
                        .append(" ").append(field.getName()).append(";\n\n")
        );

        Stream.of(clazz.getDeclaredConstructors()).forEach(constructor ->
                sb
                        .append("\t").append(Arrays.stream(constructor.getAnnotations()).map(a -> "@" + a.annotationType().getSimpleName() + "\n").collect(Collectors.joining()))
                        .append("\t").append(Modifier.toString(constructor.getModifiers()))
                        .append(" ").append(clazz.getSimpleName()).append("(")
                        .append(Stream.of(constructor.getParameters()).map(p -> GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, ArrayUtils.indexOf(constructor.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", ")))
                        .append(") { }\n\n"));

        Stream.of(clazz.getDeclaredMethods()).forEach(method -> sb
                .append("\t").append(Arrays.stream(method.getAnnotations()).map(a -> "@" + a.annotationType().getSimpleName() + "\n").collect(Collectors.joining()))
                .append("\t").append(Modifier.toString(method.getModifiers()))
                .append(" ").append(GenericTypeResolverUtil.resolveReturnTypeGenericsTree(method).printResult()).append(" ").append(method.getName()).append("(")
                .append(Stream.of(method.getParameters()).map(p -> GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method, ArrayUtils.indexOf(method.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", ")))
                .append(") { }\n\n"));

        sb.append("}");

        System.out.println(sb);
    }
}
