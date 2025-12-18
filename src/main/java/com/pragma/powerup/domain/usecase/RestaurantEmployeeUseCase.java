package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.powerup.domain.exception.RestaurantEmployeeExistsException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotEmployeeException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;

public class RestaurantEmployeeUseCase implements IRestaurantEmployeeServicePort {

    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort tokenPort;
    private final IUserGatewayPort  userGatewayPort;

    public RestaurantEmployeeUseCase(IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IUserGatewayPort userGatewayPort) {
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
        this.userGatewayPort = userGatewayPort;
    }

    @Override
    public void saveEmployee(RestaurantEmployee restaurantEmployee) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantEmployee.getIdRestaurant())
                .orElseThrow(RestaurantNotExistException::new);
        restaurant.validateOwner(tokenPort.getUserId());
        if (restaurantEmployeePersistencePort.existsByUserId(restaurantEmployee.getIdUser())){
            throw new RestaurantEmployeeExistsException();
        }
        if (Boolean.FALSE.equals( userGatewayPort.isUserEmployee(restaurantEmployee.getIdUser()))){
            throw new UserIsNotEmployeeException();
        }
        restaurantEmployeePersistencePort.saveEmployee(restaurantEmployee);
    }
}
