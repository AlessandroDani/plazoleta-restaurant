package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeePersistencePort {
    void saveEmployee(RestaurantEmployee restaurantEmployee);
}
