package com.beyt.reflection;

import com.beyt.reflection.annotation.MyFieldAutowired;
import com.beyt.reflection.controller.UserController;
import com.beyt.reflection.dto.IdentityDTOInteface;
import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.dto.UserRecord;
import com.beyt.reflection.dto.enumeration.UserStatus;
import com.beyt.reflection.service.UserService;
import com.beyt.reflection.util.GenericTypeResolverUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("fullClassPrint")
public class ReflectionFullClassPrintExample implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
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
        System.out.println("-------- Interface -------------");
        printClass(IdentityDTOInteface.class);
        System.out.println("--------- Enum ------------");
        printClass(UserStatus.class);
        System.out.println("--------- Annotation ------------");
        printClass(MyFieldAutowired.class);
        System.out.println("--------- Record ------------");
        printClass(UserRecord.class);
    }

    private static void printClass(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        sb
                .append("package ")
                .append(clazz.getPackage().getName())
                .append(";\n\n\n")
                .append(printAnnotations(clazz.getAnnotations()))
                .append(Modifier.toString(clazz.getModifiers()))
                .append(clazz.isEnum() ? " enum " : clazz.isRecord() ? " record " : clazz.isAnnotation() || clazz.isInterface() ? " " : " class ")
                .append(clazz.getSimpleName())
                .append(" {\n\n");

        Stream.of(clazz.getDeclaredFields()).forEach(field ->
                sb
                        .append("\t")
                        .append(printAnnotations(field.getAnnotations()))
                        .append("\t")
                        .append(Modifier.toString(field.getModifiers()))
                        .append(" ")
                        .append(GenericTypeResolverUtil.resolveFieldGenericsTree(field).printResult())
                        .append(" ")
                        .append(field.getName())
                        .append(";\n\n")
        );

        Stream.of(clazz.getDeclaredConstructors()).forEach(constructor ->
                sb
                        .append("\t")
                        .append(printAnnotations(constructor.getAnnotations()))
                        .append("\t")
                        .append(Modifier.toString(constructor.getModifiers()))
                        .append(" ")
                        .append(clazz.getSimpleName())
                        .append("(")
                        .append(printConstructorParameter(constructor))
                        .append(") { }\n\n"));

        Stream.of(clazz.getDeclaredMethods()).forEach(method ->
                sb
                        .append("\t")
                        .append(printAnnotations(method.getAnnotations()))
                        .append("\t")
                        .append(Modifier.toString(method.getModifiers()))
                        .append(" ")
                        .append(GenericTypeResolverUtil.resolveReturnTypeGenericsTree(method).printResult())
                        .append(" ")
                        .append(method.getName())
                        .append("(")
                        .append(printMethodParameter(method))
                        .append(") { }\n\n"));

        sb
                .append("}");

        System.out.println(sb);
    }

    private static String printConstructorParameter(Constructor<?> constructor) {
        return Stream.of(constructor.getParameters()).map(p -> GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, ArrayUtils.indexOf(constructor.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", "));
    }

    private static String printMethodParameter(Method method) {
        return Stream.of(method.getParameters()).map(p -> GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method, ArrayUtils.indexOf(method.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", "));
    }

    private static String printAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations).map(a -> "@" + a.annotationType().getSimpleName() + "\n").collect(Collectors.joining());
    }
}
