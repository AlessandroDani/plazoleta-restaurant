package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlateEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlateJpaAdapter implements IPlatePersistencePort {

    private final IPlateRepository plateRepository;
    private final IPlateEntityMapper plateEntityMapper;

    @Override
    public void savePlate(Plate plate) {
        PlateEntity plateEntity = plateEntityMapper.toEntity(plate);
        plateRepository.save(plateEntity);
    }
}
