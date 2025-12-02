package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainValidateException;
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
        validateName(restaurant.getName());
        validatePhoneNumber(restaurant.getPhoneNumber());
        validateOwnerRole(restaurant.getIdOwner());

        restaurantPersistence.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantByNit(String nit) {
        return restaurantPersistence.getRestaurantByNit(nit);
    }

    private void validateOwnerRole(Long ownerId) {
        userGatewayPort.isUserOwner(ownerId);
    }

    private void validateName(String name) {
        if (name.matches("^\\d+$")) {
            throw new DomainValidateException("El nombre del restaurant no puede ser solo números");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 13) {
            throw new DomainValidateException("El número de telefono no puede ser mayor de 13 caracteres");
        }

        if (!phoneNumber.matches("^\\+?\\d+$")) {
            throw new DomainValidateException("Formato de telefono no válido");
        }
    }
}
