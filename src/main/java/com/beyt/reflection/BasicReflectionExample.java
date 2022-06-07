package com.beyt.reflection;

import com.beyt.reflection.dto.BaseDTO;
import com.beyt.reflection.dto.IdentityDTOInteface;
import com.beyt.reflection.dto.UserDTO;

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

        } catch (Exception e) {
            // ignore
        }
    }
}
