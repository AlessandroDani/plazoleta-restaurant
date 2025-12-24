package com.pragma.powerup.application.handler;


import com.pragma.powerup.application.dto.request.RestaurantEmployeeRequestDto;

public interface IRestaurantEmployeeHandler {
    void saveEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long id);
}
