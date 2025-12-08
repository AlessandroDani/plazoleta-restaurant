package com.pragma.powerup.domain.spi;


import com.pragma.powerup.domain.model.Order;

public interface IOrderPersistencePort {
    void saveOrder(Order order);

    boolean hasActiveOrder(Long userId);
}
