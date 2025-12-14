package com.pragma.powerup.infrastructure.out.http.mapper;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.infrastructure.out.http.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {

    User toModel(UserResponseDto userResponseDto);
}
