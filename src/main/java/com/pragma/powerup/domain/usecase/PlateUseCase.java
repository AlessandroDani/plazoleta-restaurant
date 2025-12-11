package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.CategoryNotFoundException;
import com.pragma.powerup.domain.exception.PlateAlreadyExistException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;

import java.util.List;


public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort tokenPort;
    private final ICategoryPersistencePort  categoryPersistencePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, ICategoryPersistencePort categoryPersistencePort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public void savePlate(Plate plate) {
        validateRestaurantAndOwner(plate.getIdRestaurant(), tokenPort.getUserId());
        if(!categoryPersistencePort.existsCategoryById(plate.getIdCategory())){
            throw new CategoryNotFoundException();
        }
        if (platePersistencePort.existsPlateByName(plate.getName())) {
            throw new PlateAlreadyExistException();
        }
        plate.setActive(true);
        platePersistencePort.savePlate(plate);
    }


    @Override
    public void updatePlate(Long newPrice, String newDescription, Long idPlate) {
        Plate newPlate = getPlateAndValidateOwner(idPlate);
        newPlate.updateDetails(newPrice, newDescription);
        platePersistencePort.updatePlate(newPlate);
    }

    @Override
    public void updateActivePlate(boolean status, Long idPlate) {
        Plate newPlate = getPlateAndValidateOwner(idPlate);
        newPlate.setActive(status);
        platePersistencePort.updatePlate(newPlate);
    }

    @Override
    public List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category) {
        restaurantPersistencePort.getRestaurantById(idRestaurant).
                orElseThrow(RestaurantNotExistException::new);
        return platePersistencePort.getPlatesByRestaurant(idRestaurant, page, size, category);
    }

    private Plate getPlateAndValidateOwner(Long idPlate) {
        Plate plate = platePersistencePort.getPlateById(idPlate).orElseThrow(PlateNotFoundException::new);
        validateRestaurantAndOwner(plate.getIdRestaurant(), tokenPort.getUserId());
        return plate;
    }

    private void validateRestaurantAndOwner(Long idRestaurant, Long idUser){
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(idRestaurant).
                orElseThrow(RestaurantNotExistException::new);
        restaurant.validateOwner(idUser);
    }
}
