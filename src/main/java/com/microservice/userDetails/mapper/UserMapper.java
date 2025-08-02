package com.microservice.userDetails.mapper;

import com.microservice.userDetails.dao.User;
import com.microservice.userDetails.dto.UserDto;
import com.microservice.userDetails.dto.UserRegistrationDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "lastLoginLocation", ignore = true)
    @Mapping(target = "lastLoginBrowser", ignore = true)
    User fromRegistrationDto(UserRegistrationDto userRegistrationDto);

    List<UserDto> toDtoList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserDto userDto, @MappingTarget User user);
}
