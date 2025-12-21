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
        Long userId = getUserIdFromToken();
        Restaurant restaurant = validateRestaurant(order.getIdRestaurant());
        validateOrderStatus(userId);
        restaurant.validatePlateList(order.getPlates(), platePersistencePort.getPlatesIdsByRestaurant(restaurant.getId()));
        order.initializeNewOrder(userId, LocalDateTime.now());
        Order saveOrder = orderPersistencePort.saveOrder(order);
        User client = userGatewayPort.getUserById(order.getIdClient());
        saveOrderTrace(saveOrder, client, null, "NONE", OrderStatus.PENDING.getDbValue());
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status, int page, int size) {
        Long userId = getUserIdFromToken();
        RestaurantEmployee employee = restaurantEmployeePersistencePort.getEmployee(userId)
                .orElseThrow(UserNotAssignRestaurant::new);
        return orderPersistencePort.getOrdersByRestaurantAndStatus(employee.getIdRestaurant(), status, page, size);
    }

    @Override
    public void transitionToPreparation(Long orderId) {
        Long userId = getUserIdFromToken();
        Order order = validateOrder(orderId, userId);
        order.assignToPreparation(userId);
        orderPersistencePort.saveOrder(order);
        registerStatusChangeTrace(order, OrderStatus.PENDING, OrderStatus.IN_PREPARATION);
    }

    @Override
    public void transitionToReady(Long orderId) {
        Order order = validateOrder(orderId, getUserIdFromToken());
        Integer pin = getSecurityPin();
        order.assignToReady(pin);
        orderPersistencePort.saveOrder(order);
        sendNotification(order.getIdClient(), "Tu pedido está listo. Reclámalo con el PIN: " + pin);
        registerStatusChangeTrace(order, OrderStatus.IN_PREPARATION, OrderStatus.READY);

    }

    @Override
    public void transitionToDelivered(Long orderId, Integer pin) {
        Order order = validateOrder(orderId, getUserIdFromToken());
        order.assignToDelivered(pin);
        orderPersistencePort.saveOrder(order);
        registerStatusChangeTrace(order, OrderStatus.READY, OrderStatus.DELIVERED);
    }

    @Override
    public void transitionToCanceled(Long orderId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.isOwner(getUserIdFromToken());
        try {
            order.assignToCanceled();
            orderPersistencePort.saveOrder(order);
            registerStatusChangeTrace(order, OrderStatus.PENDING, OrderStatus.CANCELED);
        } catch (OrderNotInPendingStatusException exception) {
            sendNotification(order.getIdClient(), "Lo sentimos, su pedido ya está en preparación y no puede cancelarse");
            throw exception;
        }
    }

    @Override
    public List<Traceability> getTracesByOrderId(Long orderId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.isOwner(getUserIdFromToken());
        return userGatewayPort.getTracesByOrderId(orderId);
    }

    @Override
    public List<EmployeePerformance> getEmployeePerformances(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId).
                orElseThrow(RestaurantNotExistException::new);
        restaurant.validateOwner(getUserIdFromToken());
        return userGatewayPort.getEmployeePerformance(restaurantId);
    }

    @Override
    public List<OrderEfficiency> getOrderMetrics(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(restaurantId).
                orElseThrow(RestaurantNotExistException::new);
        restaurant.validateOwner(getUserIdFromToken());
        return userGatewayPort.getOrderEfficiency(restaurantId);
    }

    private Long getUserIdFromToken() {
        return tokenPort.getUserId();
    }

    private int getSecurityPin() {
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private void sendNotification(Long clientId, String message) {
        User client = userGatewayPort.getUserById(clientId);
        userGatewayPort.sendSms(client.getPhoneNumber(), message);
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

    private Order validateOrder(Long orderId, Long userId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        RestaurantEmployee employee = restaurantEmployeePersistencePort.getEmployee(userId)
                .orElseThrow(UserNotAssignRestaurant::new);
        if (!order.getIdRestaurant().equals(employee.getIdRestaurant())) {
            throw new EmployeeDoesNotBelongToRestaurantException();
        }
        return order;
    }

    private void registerStatusChangeTrace(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        User client = userGatewayPort.getUserById(order.getIdClient());
        User employee = userGatewayPort.getUserById(getUserIdFromToken());
        saveOrderTrace(order, client, employee, oldStatus.getDbValue(), newStatus.getDbValue());
    }

    public void saveOrderTrace(Order order, User client, User employee, String lastStatus, String newStatus) {
        Long employeeId = (employee != null) ? employee.getId() : null;
        String employeeEmail = (employee != null) ? employee.getEmail() : null;

        Traceability traceModel = Traceability.builder()
                .restaurantId(order.getIdRestaurant())
                .orderId(order.getId())
                .clientId(client.getId())
                .clientEmail(client.getEmail())
                .date(LocalDateTime.now())
                .lastStatus(lastStatus)
                .newStatus(newStatus)
                .employeeId(employeeId)
                .employeeEmail(employeeEmail)
                .build();
        userGatewayPort.saveOrderTrace(traceModel);
    }
}
