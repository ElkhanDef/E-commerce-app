package com.app.service;

import com.app.model.dto.request.CategoryRequestDto;
import com.app.model.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    CategoryResponseDto create(CategoryRequestDto request);
    CategoryResponseDto update(CategoryRequestDto request, Long categoryId);
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long categoryId);
    CategoryResponseDto getCategoryBySlug(String slug);
    void deleteCategoryById(Long categoryId);
}
