package com.beyt.reflection.dto;

public interface IdentityDTOInteface {
    Long getId();

    void setId(Long id);


    default void printSomething() {
        System.out.println("something");
    }
}
