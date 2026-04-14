package com.app.mapper;

import com.app.model.dto.request.ProductRequestDto;
import com.app.model.dto.response.ProductResponseDto;
import com.app.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "images", ignore = true),
            @Mapping(target = "slug", ignore = true),
            @Mapping(target = "category", ignore = true),
    })
    ProductEntity toEntity(ProductRequestDto productDto);

    @Mappings({
            @Mapping(target = "categoryResponse", ignore = true),
            @Mapping(target = "imageUrls", ignore = true),
            @Mapping(target = "mainImageUrl", ignore = true)
    })
    ProductResponseDto toDto(ProductEntity productEntity);
}
