package com.app.mapper;

import com.app.model.dto.response.UserResponseDto;
import com.app.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "isActive", source = "active"),
            @Mapping(target = "isVerified", source = "verified")
    })
    UserResponseDto toDto(UserEntity user);
}
