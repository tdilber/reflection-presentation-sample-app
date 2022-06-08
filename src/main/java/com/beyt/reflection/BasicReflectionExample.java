package com.beyt.reflection;

import com.beyt.reflection.dto.BaseDTO;
import com.beyt.reflection.dto.IdentityDTOInteface;
import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.dto.enumeration.UserStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BasicReflectionExample {


    @SuppressWarnings("all")
    public static void main(String[] args) {
        try {
            Class<UserDTO> clazz = (Class<UserDTO>) Class.forName("com.beyt.reflection.dto.UserDTO");

            UserDTO userDTO = clazz.newInstance();

            System.out.println(userDTO.toString());

            System.out.println(clazz.getClass().getClass().getClass().getSimpleName());

            System.out.println(IdentityDTOInteface.class.isAssignableFrom(userDTO.getClass()));
            System.out.println(userDTO.getClass().isAssignableFrom(IdentityDTOInteface.class));

            System.out.println(userDTO instanceof IdentityDTOInteface);
            System.out.println(userDTO instanceof BaseDTO);

            UserStatus[] enumConstants = UserStatus.class.getEnumConstants();
            System.out.println(Arrays.asList(enumConstants).stream().map(Object::toString).collect(Collectors.joining(", ")));

            System.out.println("Super class " + clazz.getSuperclass() + "\nSuper class " + clazz.getSuperclass().getSuperclass());

            System.out.println("Interfaces class: " + printInterfaces(clazz.getInterfaces()));

            System.out.println("Super Interfaces class: " + printInterfaces(clazz.getSuperclass().getInterfaces()));
        } catch (Exception e) {
            // ignore
        }
    }

    private static String printInterfaces(Class<?>[] interfaces) {
        if (interfaces.length == 0) {
            return "none";
        }

        return Arrays.stream(interfaces).map(Class::toString).collect(Collectors.joining(", "));
    }
}
