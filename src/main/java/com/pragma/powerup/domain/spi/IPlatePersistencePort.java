package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plate;

import java.util.List;
import java.util.Optional;

public interface IPlatePersistencePort {
    void savePlate(Plate plate);
    Optional<Plate> getPlateById(Long id);
    boolean existsPlateByName(String name);
    void updatePlate(Plate plate);
    List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category);
}
