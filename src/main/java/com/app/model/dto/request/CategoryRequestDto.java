package com.app.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequestDto {

    @NotBlank(message = "Kategori ismi boş olamaz")
    @Size(min = 2, max = 50, message = "Kategori ismi 2-50 karakter arasında olmalıdır")
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
