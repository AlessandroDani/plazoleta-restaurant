package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

import java.util.Objects;


public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort,  IRestaurantPersistencePort restaurantPersistencePort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void savePlate(Plate plate, Long idOwnerRequest) {
        validateRestaurantAndRole(plate, idOwnerRequest);
        plate.setActive(true);
        platePersistencePort.savePlate(plate);
    }

    @Override
    public void updatePlate(Long id, Long newPrice, String newDescription, Long idOwner) {
        Plate plate =  platePersistencePort.getPlateById(id);
        if(plate == null){
            throw new PlateNotFoundException();
        }

        validateRestaurantAndRole(plate, idOwner);
        if(newPrice != null){
            plate.setPrice(newPrice);
        }

        if(newDescription != null){
            plate.setDescription(newDescription);
        }
        platePersistencePort.updatePlate(plate);
    }

    public void validateRestaurantAndRole(Plate plate, Long idOwner) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(plate.getIdRestaurant());
        if(restaurant == null){
            throw new RestaurantNotExistException();
        }
        if (!Objects.equals(restaurant.getIdOwner(), idOwner)){
            throw new UserIsNotOwnerRestaurantException();
        }
    }
}
