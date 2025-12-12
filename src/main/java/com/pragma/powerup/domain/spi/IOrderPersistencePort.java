package com.pragma.powerup.domain.spi;


import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    void saveOrder(Order order);
    boolean hasActiveOrder(Long userId);
    Optional<List<Order>> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status, int page, int size);
}
