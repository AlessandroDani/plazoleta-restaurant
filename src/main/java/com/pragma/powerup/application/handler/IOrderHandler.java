package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.OrderStatus;

import java.util.List;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getOrdersByStatus(OrderStatus orderStatus, int page, int size);
    void assignOrderAndChangeStatus(Long id);
    void notifyOrderReady(Long orderId);
    void transitionToDelivered(Long orderId, String securityPin);
}
