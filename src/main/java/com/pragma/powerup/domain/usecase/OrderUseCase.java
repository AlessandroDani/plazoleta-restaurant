package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderUseCase implements IOrderServicePort {
    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ITokenPort tokenPort;
    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IUserGatewayPort userGatewayPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IPlatePersistencePort platePersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserGatewayPort userGatewayPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.tokenPort = tokenPort;
        this.platePersistencePort = platePersistencePort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.userGatewayPort = userGatewayPort;
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
    public List<Order> getOrdersByStatus(OrderStatus status, int page, int size) {
        Long userId = tokenPort.getUserId();
        RestaurantEmployee employee = restaurantEmployeePersistencePort.getEmployee(userId)
                .orElseThrow(EmployeeDoesNotBelongToRestaurantException::new);
        return orderPersistencePort.getOrdersByRestaurantAndStatus(employee.getIdRestaurant(), status, page, size);
    }

    @Override
    public void assignOrderAndChangeStatus(Long orderId) {
        Long userId = tokenPort.getUserId();
        Order order = checkOrder(orderId, userId);
        order.assignToPreparation(userId);
        orderPersistencePort.saveOrder(order);
    }

    @Override
    public void notifyOrderReady(Long orderId) {
        Order order = checkOrder(orderId, tokenPort.getUserId());
        Integer pin = generateSecurityPin();
        order.assignToReady(pin);
        orderPersistencePort.saveOrder(order);
        sendNotification(order.getIdClient(),
                "Tu pedido está listo. Reclámalo con el PIN: " + pin);
    }

    @Override
    public void transitionToDelivered(Long orderId, Integer pin) {
        Order order = checkOrder(orderId, tokenPort.getUserId());
        order.assignToDelivered(pin);
        orderPersistencePort.saveOrder(order);
    }

    @Override
    public void transitionToCanceled(Long orderId) {
        Long userId = tokenPort.getUserId();
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.isOwner(userId);
        try {
            order.assignToCanceled();
            orderPersistencePort.saveOrder(order);
        } catch (OrderNotInPendingStatusException exception) {
            sendNotification(order.getIdClient(),
                    "Lo sentimos, su pedido ya está en preparación y no puede cancelarse");
            throw exception;
        }
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

    private int generateSecurityPin() {
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private Order checkOrder(Long orderId, Long userId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        RestaurantEmployee employee = restaurantEmployeePersistencePort.getEmployee(userId)
                .orElseThrow(EmployeeDoesNotBelongToRestaurantException::new);
        if (!order.getIdRestaurant().equals(employee.getIdRestaurant())) {
            throw new EmployeeDoesNotBelongToRestaurantException();
        }
        return order;
    }

    private void sendNotification(Long clientId, String message) {
        User client = userGatewayPort.getUserById(clientId);
        userGatewayPort.sendSms(client.getPhoneNumber(), message);
    }
}
