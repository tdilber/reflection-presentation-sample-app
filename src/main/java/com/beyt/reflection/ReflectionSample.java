package com.beyt.reflection;

import com.beyt.reflection.controller.UserController;
import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.service.UserService;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionSample {
    public static void main(String[] args) {
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
                sb.append("\t").append(Modifier.toString(field.getModifiers())).append(" ").append(field.getType().getSimpleName())
                        .append(" ").append(field.getName()).append(";\n\n")
        );

        Stream.of(clazz.getDeclaredConstructors()).forEach(constructor -> sb.append("\t").append(Modifier.toString(constructor.getModifiers()))
                .append(" ").append(clazz.getSimpleName()).append("(")
                .append(Stream.of(constructor.getParameters()).map(p -> p.getType().getSimpleName() + " " + p.getName()).collect(Collectors.joining(", ")))
                .append(") { }\n\n"));

        Stream.of(clazz.getDeclaredMethods()).forEach(method -> sb.append("\t").append(Modifier.toString(method.getModifiers()))
                .append(" ").append(method.getReturnType().getSimpleName()).append(" ").append(method.getName()).append("(")
                .append(Stream.of(method.getParameters()).map(p -> p.getType().getSimpleName() + " " + p.getName()).collect(Collectors.joining(", ")))
                .append(") { }\n\n"));

        sb.append("}");

        System.out.println(sb);
    }
}
