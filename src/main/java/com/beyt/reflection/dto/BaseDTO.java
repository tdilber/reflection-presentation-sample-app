package com.beyt.reflection.dto;

public abstract class BaseDTO implements IdentityDTOInteface {
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
