package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final IUserGatewayPort userGatewayPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence, IUserGatewayPort userGatewayPort) {
        this.restaurantPersistence = restaurantPersistence;
        this.userGatewayPort = userGatewayPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if(getRestaurantByNit(restaurant.getNit()) != null){
            throw new RestaurantAlreadyExistException();
        }
        validateOwnerRole(restaurant.getIdOwner());
        restaurantPersistence.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantByNit(String nit) {
        return restaurantPersistence.getRestaurantByNit(nit);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return null;
    }

    private void validateOwnerRole(Long ownerId) {
        userGatewayPort.isUserOwner(ownerId);
    }
}
