package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final IUserGatewayPort  userGateway;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence, IUserGatewayPort userGateway) {
        this.restaurantPersistence = restaurantPersistence;
        this.userGateway = userGateway;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurantPersistence.getRestaurantByNit(restaurant.getNit()) != null) {
            throw new RestaurantAlreadyExistException();
        }
        userGateway.isUserOwner(restaurant.getIdOwner());
        restaurantPersistence.saveRestaurant(restaurant);
    }
}
