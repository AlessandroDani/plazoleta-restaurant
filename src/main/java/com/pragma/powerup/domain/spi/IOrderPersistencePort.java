package com.pragma.powerup.domain.spi;


import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderStatus;

import java.util.List;

public interface IOrderPersistencePort {
    void saveOrder(Order order);
    boolean hasActiveOrder(Long userId);
    List<Order> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status, int page, int size);
}
