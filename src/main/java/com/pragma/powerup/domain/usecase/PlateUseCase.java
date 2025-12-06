package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.PlateAlreadyExistException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;

import java.util.Objects;


public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort tokenPort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
    }

    @Override
    public void savePlate(Plate plate) {
        Long id = tokenPort.getUserId();
        validateRestaurantAndOwner(plate, id);
        if (platePersistencePort.getPlateByName(plate.getName()) != null) {
            throw new PlateAlreadyExistException();
        }
        plate.setActive(true);
        platePersistencePort.savePlate(plate);
    }


    @Override
    public void updatePlate(Long newPrice, String newDescription, Long idPlate) {
        Plate newPlate = validPlate(platePersistencePort.getPlateById(idPlate));
        if (newPrice != null) {
            newPlate.setPrice(newPrice);
        }

        if (newDescription != null) {
            newPlate.setDescription(newDescription);
        }
        platePersistencePort.updatePlate(newPlate);
    }

    @Override
    public void updateActivePlate(boolean status, Long idPlate) {
        Plate newPlate = validPlate(platePersistencePort.getPlateById(idPlate));
        newPlate.setActive(status);
        platePersistencePort.updatePlate(newPlate);
    }

    public void validateRestaurantAndOwner(Plate plate, Long id) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(plate.getIdRestaurant());
        if (restaurant == null) {
            throw new RestaurantNotExistException();
        }
        if (!Objects.equals(restaurant.getIdOwner(), id)) {
            throw new UserIsNotOwnerRestaurantException();
        }
    }

    public Plate validPlate(Plate plate) {
        Long idUser = tokenPort.getUserId();
        if (plate == null) {
            throw new PlateNotFoundException();
        }
        validateRestaurantAndOwner(plate, idUser);
        return plate;
    }
}
