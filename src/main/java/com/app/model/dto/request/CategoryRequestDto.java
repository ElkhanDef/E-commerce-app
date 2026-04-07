package com.app.model.dto.request;

public class CategoryRequestDto {

    private String name;

    public CategoryRequestDto(String name) {
        this.name = name;
    }

    public CategoryRequestDto() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
