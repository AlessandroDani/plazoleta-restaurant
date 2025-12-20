package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.domain.model.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateResponseMapper {
    PlateResponseDto toResponse(Plate plate);
    List<PlateResponseDto> toResponseList(List<Plate> plates);
}
