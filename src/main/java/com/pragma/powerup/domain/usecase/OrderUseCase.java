package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.EmployeeNotValidException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserHasActiveOrderException;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort tokenPort;
    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IPlatePersistencePort platePersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
        this.platePersistencePort = platePersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
    }

    @Override
    public void saveOrder(Order order) {
        Long userId = tokenPort.getUserId();
        Restaurant restaurant = validateRestaurant(order.getIdRestaurant());
        validateOrderStatus(userId);
        restaurant.validatePlateList(order.getPlates(), platePersistencePort);

        order.initializeNewOrder(userId, LocalDateTime.now());
        orderPersistencePort.saveOrder(order);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status, int page, int  size) {
        Long userId = tokenPort.getUserId();
        RestaurantEmployee employee = restaurantEmployeePersistencePort.getEmployee(userId)
                .orElseThrow(EmployeeNotValidException::new);
        return orderPersistencePort.getOrdersByRestaurantAndStatus(employee.getIdRestaurant(), status, page, size);
    }

    private Restaurant validateRestaurant(Long idRestaurant) {
        return restaurantPersistencePort.getRestaurantById(idRestaurant)
                .orElseThrow(RestaurantNotExistException::new);
    }

    private void validateOrderStatus(Long userId) {
        if (orderPersistencePort.hasActiveOrder(userId)) {
            throw new UserHasActiveOrderException();
        }
    }
}
