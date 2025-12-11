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

import java.util.List;
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
        validPlate(plate, id);
        if (platePersistencePort.existsPlateByName(plate.getName())) {
            throw new PlateAlreadyExistException();
        }
        plate.setActive(true);
        platePersistencePort.savePlate(plate);
    }


    @Override
    public void updatePlate(Long newPrice, String newDescription, Long idPlate) {
        Long userId = tokenPort.getUserId();
        Plate newPlate = validPlate(platePersistencePort.getPlateById(idPlate), userId);
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
        Long userId = tokenPort.getUserId();
        Plate newPlate = validPlate(platePersistencePort.getPlateById(idPlate), userId);
        newPlate.setActive(status);
        platePersistencePort.updatePlate(newPlate);
    }

    @Override
    public List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category) {
        restaurantPersistencePort.getRestaurantById(idRestaurant).
                orElseThrow(RestaurantNotExistException::new);

        List<Plate> plateList = platePersistencePort.getPlatesByRestaurant(idRestaurant, page, size, category);
        if (plateList.isEmpty()) {
            throw new PlateNotFoundException();
        }
        return plateList;
    }

    public Plate validPlate(Plate plate, Long userId) {
        if (plate == null) {
            throw new PlateNotFoundException();
        }
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(plate.getIdRestaurant()).
                orElseThrow(RestaurantNotExistException::new);
        if (!Objects.equals(restaurant.getIdOwner(), userId)) {
            throw new UserIsNotOwnerRestaurantException();
        }
        return plate;
    }
}
