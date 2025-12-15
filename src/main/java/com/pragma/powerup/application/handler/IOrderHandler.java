package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;

import java.util.List;

public interface IOrderHandler {
    void saveOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getOrdersByStatus(OrderStatus orderStatus, int page, int size);
    void assignOrderAndChangeStatus(Long id);
    void notifyOrderReady(Long orderId);
    void transitionToDelivered(Long orderId, Integer securityPin);
    void transitionToCanceled(Long orderId);
    List<TraceabilityResponseDto> getTraceability(Long orderId);
    List<OrderEfficiencyResponseDto> getOrderMetrics();
    List<EmployeePerformanceResponseDto> getEmployeePerformances();

}
