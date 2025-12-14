package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderStatus;

import java.util.List;

public interface IOrderServicePort {
    void saveOrder(Order order);
    List<Order> getOrdersByStatus(OrderStatus status, int page, int size);
    void assignOrderAndChangeStatus(Long plateId);
    void notifyOrderReady(Long orderId);
    void transitionToDelivered(Long orderId, String securityPin);
}
