package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.PlatePathActiveRequestDto;
import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import com.pragma.powerup.application.mapper.IPlateRequestMapper;
import com.pragma.powerup.application.mapper.IPlateResponseMapper;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.model.Plate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlateHandler implements IPlateHandler {

    private final IPlateServicePort plateServicePort;
    private final IPlateRequestMapper plateRequestMapper;
    private final IPlateResponseMapper  plateResponseMapper;

    @Override
    public void savePlate(PlateRequestDto plateRequestDto) {
        Plate plate = plateRequestMapper.toPlate(plateRequestDto);
        plateServicePort.savePlate(plate);
    }

    @Override
    public void updatePlate(PlateUpdateRequestDto plateUpdateRequestDto, Long idPlate) {
        plateServicePort.updatePlate(
                plateUpdateRequestDto.getPrice(),
                plateUpdateRequestDto.getDescription(),
                idPlate
        );
    }

    @Override
    public void updateStatusPlate(PlatePathActiveRequestDto platePathActiveRequestDto, Long idPlate) {
        plateServicePort.updateActivePlate(
                platePathActiveRequestDto.isActive(),
                idPlate
        );
    }

    @Override
    public List<PlateResponseDto> getPlatesByRestaurant(Long id, int page, int size, String category) {
        List<Plate> plates = plateServicePort.getPlatesByRestaurant(id, page, size, category);
        return plateResponseMapper.toResponseList(plates);

    }
}
