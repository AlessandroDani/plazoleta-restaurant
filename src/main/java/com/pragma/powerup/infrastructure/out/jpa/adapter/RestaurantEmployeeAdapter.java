package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;


public class RestaurantEmployeeAdapter implements IRestaurantEmployeePersistencePort {
    private final IRestaurantEmployeeRepository  restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    public RestaurantEmployeeAdapter(IRestaurantEmployeeRepository restaurantEmployeeRepository, IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper) {
        this.restaurantEmployeeRepository = restaurantEmployeeRepository;
        this.restaurantEmployeeEntityMapper = restaurantEmployeeEntityMapper;
    }

    @Override
    public void saveEmployee(RestaurantEmployee restaurantEmployee) {
        restaurantEmployeeRepository.save(restaurantEmployeeEntityMapper.toEntity(restaurantEmployee));
    }
}
