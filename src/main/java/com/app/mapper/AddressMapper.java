package com.app.mapper;

import com.app.model.dto.request.AddressRequestDto;
import com.app.model.dto.response.AddressResponseDto;
import com.app.model.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mappings({
            @Mapping(target = "id",  ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    AddressEntity toEntity (AddressRequestDto addressDto);

    AddressResponseDto toDto (AddressEntity address);

    @Mappings({
            @Mapping(target = "id",  ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true)
    })
    AddressEntity updateEntityFromDto(AddressRequestDto dto, @MappingTarget AddressEntity entity);
}
