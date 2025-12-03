package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.domain.model.Plate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlateRequestMapper {

    Plate toPlate(PlateRequestDto plateRequestDto);
}
