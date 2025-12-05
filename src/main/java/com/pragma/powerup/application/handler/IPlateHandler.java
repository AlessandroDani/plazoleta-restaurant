package com.pragma.powerup.application.handler;


import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;

public interface IPlateHandler {
    void savePlate(PlateRequestDto plateRequestDto);
    void updatePlate(PlateUpdateRequestDto plateUpdateRequestDto, Long idPlate);
}
