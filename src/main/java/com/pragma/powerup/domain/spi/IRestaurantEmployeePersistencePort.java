package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantEmployee;

import java.util.Optional;

public interface IRestaurantEmployeePersistencePort {
    void saveEmployee(RestaurantEmployee restaurantEmployee);
    Optional<RestaurantEmployee> getEmployee(Long userId);
    boolean existsByUserId(Long idUser);
}
