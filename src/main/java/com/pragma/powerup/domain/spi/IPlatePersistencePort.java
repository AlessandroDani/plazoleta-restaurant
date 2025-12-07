package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plate;

import java.util.List;

public interface IPlatePersistencePort {
    void savePlate(Plate plate);
    Plate getPlateById(Long id);
    Plate getPlateByName(String name);
    void updatePlate(Plate plate);
    List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category);
}
