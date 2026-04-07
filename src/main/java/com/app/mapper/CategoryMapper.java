package com.app.mapper;

import com.app.model.dto.request.CategoryRequestDto;
import com.app.model.dto.request.CategoryResponseDto;
import com.app.model.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponseDto toDto(CategoryEntity categoryEntity);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "products", ignore = true)
    })
    CategoryEntity toEntity(CategoryRequestDto dto);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "products", ignore = true)
    })
    CategoryEntity update(CategoryRequestDto requestDto, @MappingTarget CategoryEntity categoryEntity);

    List<CategoryResponseDto> toDtoList(List<CategoryEntity> categories);
}
