package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.PlateBelongsToAnotherRestaurantException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserHasActiveOrderException;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;

import java.time.LocalDate;
import java.util.Objects;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort  tokenPort;
    private final IPlatePersistencePort platePersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IPlatePersistencePort platePersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
        this.platePersistencePort = platePersistencePort;
    }


    @Override
    public void saveOrder(Order order) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(order.getIdRestaurant());
        if (restaurant == null) {
            throw new RestaurantNotExistException();
        }
        if(orderPersistencePort.hasActiveOrder(tokenPort.getUserId())){
            throw new UserHasActiveOrderException();
        }

        for(OrderPlate orderPlate : order.getOrders()){
            Plate plate = platePersistencePort.getPlateById(orderPlate.getIdPlate());
            if(plate == null){
                throw new PlateNotFoundException();
            }
            if(!Objects.equals(restaurant.getId(), plate.getIdRestaurant())){
                throw new PlateBelongsToAnotherRestaurantException();
            }
        }

        order.setDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING.getDbValue());
        orderPersistencePort.saveOrder(order);
    }
}
