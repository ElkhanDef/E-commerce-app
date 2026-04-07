package com.app.service.impl;

import com.app.exception.ApplicationException;
import com.app.exception.data.ErrorCode;
import com.app.mapper.CategoryMapper;
import com.app.model.dto.request.CategoryRequestDto;
import com.app.model.dto.request.CategoryResponseDto;
import com.app.model.entity.CategoryEntity;
import com.app.repository.CategoryRepository;
import com.app.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto request) {
        log.info("ActionLog.create.start");
        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.toEntity(request);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        log.info("ActionLog.create.end");
        return CategoryMapper.INSTANCE.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDto update(CategoryRequestDto request, Long categoryId) {
        log.info("ActionLog.update.start");
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));
        CategoryEntity updatedCategory = CategoryMapper.INSTANCE.update(request, category);
        CategoryEntity savedCategory = categoryRepository.save(updatedCategory);
        log.info("ActionLog.update.end");
        return CategoryMapper.INSTANCE.toDto(savedCategory);
    }

    public CategoryResponseDto getCategoryById(Long categoryId) {
        log.info("ActionLog.getCategoryById.start");
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND));
        log.info("ActionLog.getCategoryById.end");
        return CategoryMapper.INSTANCE.toDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        log.info("ActionLog.getAllCategories.start");
        List<CategoryEntity> categories = categoryRepository.findAll();
        log.info("ActionLog.getAllCategories.end");
        return CategoryMapper.INSTANCE.toDtoList(categories);
    }
}
