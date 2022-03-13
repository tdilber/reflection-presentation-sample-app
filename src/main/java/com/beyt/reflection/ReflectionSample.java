package com.beyt.reflection;

import com.beyt.reflection.controller.UserController;
import com.beyt.reflection.dto.BaseDTO;
import com.beyt.reflection.dto.IdentityDTOInteface;
import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.service.UserService;
import com.beyt.reflection.util.GenericTypeResolverUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionSample {
    @SuppressWarnings("all")
    public static void main(String[] args) {
        try {
            Class<UserDTO> clazz = (Class<UserDTO>)Class.forName("com.beyt.reflection.dto.UserDTO");

            UserDTO userDTO  = clazz.newInstance();

            System.out.println(userDTO.toString());

            System.out.println(clazz.getClass().getClass().getClass().getSimpleName());

            System.out.println(IdentityDTOInteface.class.isAssignableFrom(userDTO.getClass()));
            System.out.println(userDTO.getClass().isAssignableFrom(IdentityDTOInteface.class));

            System.out.println(userDTO instanceof IdentityDTOInteface);
            System.out.println(userDTO instanceof BaseDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }

       // if(true) return;



        System.out.println("---------------------");
        printClass(UserController.class);
        System.out.println("---------------------");
        printClass(UserService.class);
        System.out.println("---------------------");
        printClass(UserDTO.class);
    }

    private static void printClass(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(clazz.getPackage().getName()).append(";\n\n\n")
                .append(Modifier.toString(clazz.getModifiers())).append(" class ").append(clazz.getSimpleName())
                .append(" {\n\n");

        Stream.of(clazz.getDeclaredFields()).forEach(field ->
                sb.append("\t").append(Modifier.toString(field.getModifiers())).append(" ").append(GenericTypeResolverUtil.resolveFieldGenericsTree(field))
                        .append(" ").append(field.getName()).append(";\n\n")
        );

        Stream.of(clazz.getDeclaredConstructors()).forEach(constructor -> sb.append("\t").append(Modifier.toString(constructor.getModifiers()))
                .append(" ").append(clazz.getSimpleName()).append("(")
                .append(Stream.of(constructor.getParameters()).map(p -> GenericTypeResolverUtil.resolveConstructorParameterGenericsTree(constructor, ArrayUtils.indexOf(constructor.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", ")))
                .append(") { }\n\n"));

        Stream.of(clazz.getDeclaredMethods()).forEach(method -> sb.append("\t").append(Modifier.toString(method.getModifiers()))
                .append(" ").append(GenericTypeResolverUtil.resolveReturnTypeGenericsTree(method).printResult()).append(" ").append(method.getName()).append("(")
                .append(Stream.of(method.getParameters()).map(p -> GenericTypeResolverUtil.resolveMethodParameterGenericsTree(method, ArrayUtils.indexOf(method.getParameters(), p)).printResult() + " " + p.getName()).collect(Collectors.joining(", ")))
                .append(") { }\n\n"));

        sb.append("}");

        System.out.println(sb);
    }
}
