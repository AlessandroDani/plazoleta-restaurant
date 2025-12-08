package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.domain.model.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateResponseMapper {
    PlateResponseDto toResponse(Plate plate);

    default List<PlateResponseDto> toResponseList(List<Plate> plates) {
        return plates.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
