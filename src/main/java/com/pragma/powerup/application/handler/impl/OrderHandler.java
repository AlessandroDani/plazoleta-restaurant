package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.ITraceabilityResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderRequestMapper  orderRequestMapper;
    private final IOrderServicePort orderServicePort;
    private final ITraceabilityResponseMapper traceabilityResponseMapper;


    @Override
    public void saveOrder(OrderRequestDto orderRequestDto) {
        Order order =  orderRequestMapper.toOrder(orderRequestDto);
        List<OrderPlate> list = orderRequestMapper.toOrderPlatesList(orderRequestDto.getPlates());
        order.setPlates(list);
        orderServicePort.saveOrder(order);
    }

    @Override
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus orderStatus, int page, int size) {
        List<Order> orders = orderServicePort.getOrdersByStatus(orderStatus, page, size);
        return orderRequestMapper.toResponseList(orders);
    }

    @Override
    public void assignOrderAndChangeStatus(Long orderId) {
        orderServicePort.assignOrderAndChangeStatus(orderId);
    }

    @Override
    public void notifyOrderReady(Long orderId) {
        orderServicePort.notifyOrderReady(orderId);
    }

    @Override
    public void transitionToDelivered(Long orderId, Integer securityPin) {
        orderServicePort.transitionToDelivered(orderId, securityPin);
    }

    @Override
    public void transitionToCanceled(Long orderId) {
        orderServicePort.transitionToCanceled(orderId);
    }

    @Override
    public List<TraceabilityResponseDto> getTraceability(Long orderId) {
        List<Traceability> traceResponse = orderServicePort.getTracesByOrderId(orderId);
        return traceabilityResponseMapper.toResponseDtoList(traceResponse);
    }

    @Override
    public List<OrderEfficiencyResponseDto> getOrderMetrics(Long restaurantId) {
        List<OrderEfficiency> orderResponse = orderServicePort.getOrderMetrics(restaurantId);
        return traceabilityResponseMapper.toResponseOrderDtoList(orderResponse);
    }

    @Override
    public List<EmployeePerformanceResponseDto> getEmployeePerformances(Long restaurantId) {
        List<EmployeePerformance> employeeResponse = orderServicePort.getEmployeePerformances(restaurantId);
        return traceabilityResponseMapper.toResponseEmployeeDtoList(employeeResponse);
    }

}
