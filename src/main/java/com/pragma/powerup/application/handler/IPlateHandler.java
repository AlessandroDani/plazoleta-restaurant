package com.pragma.powerup.application.handler;


import com.pragma.powerup.application.dto.request.PlatePathActiveRequestDto;
import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;

import java.util.List;

public interface IPlateHandler {
    void savePlate(PlateRequestDto plateRequestDto);
    void updatePlate(PlateUpdateRequestDto plateUpdateRequestDto, Long idPlate);
    void updateStatusPlate(PlatePathActiveRequestDto platePathActiveRequestDto, Long idPlate);
    List<PlateResponseDto> getPlatesByRestaurant(Long id, int page, int size, String category);
}
