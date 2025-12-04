package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Plate;

public interface IPlateServicePort {
    void savePlate(Plate plate, Long idOwner);
    void updatePlate(Long id, Long newPrice, String neDescription, Long idOwner);
}
