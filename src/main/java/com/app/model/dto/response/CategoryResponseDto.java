package com.app.model.dto.response;

public class CategoryResponseDto {

    private String name;
    private String slug;

    public CategoryResponseDto(String name) {
        this.name = name;
    }

    public CategoryResponseDto() {}

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
