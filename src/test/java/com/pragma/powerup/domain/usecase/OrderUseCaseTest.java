package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ITokenPort tokenPort;

    @Mock
    private IPlatePersistencePort platePersistencePort;

    @Mock
    private IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    @Mock
    private IUserGatewayPort userGatewayPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long CLIENT_ID = 200L;
    private static final Long EMPLOYEE_ID = 300L;
    private static final Long PLATE_ID_1 = 10L;
    private static final Long PLATE_ID_2 = 11L;
    private static final Long ORDER_ID = 50L;
    private static final Integer SECURITY_PIN = 1234;

    private Order testOrder;
    private User testClient;
    private User testEmployeeUser;
    private Restaurant testRestaurant;
    private RestaurantEmployee testEmployee;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(RESTAURANT_ID);
        testRestaurant.setIdOwner(1L);
        testRestaurant.setName("Test Restaurant");

        testEmployee = new RestaurantEmployee();
        testEmployee.setIdRestaurant(RESTAURANT_ID);
        testEmployee.setIdUser(EMPLOYEE_ID);

        testOrder = new Order();
        testOrder.setId(ORDER_ID);
        testOrder.setIdClient(CLIENT_ID);
        testOrder.setIdRestaurant(RESTAURANT_ID);
        testOrder.setStatus(OrderStatus.PENDING);

        testClient = new User();
        testClient.setId(CLIENT_ID);
        testClient.setEmail("cliente@test.com");
        testClient.setPhoneNumber("+573001234567");

        testEmployeeUser = new User();
        testEmployeeUser.setId(EMPLOYEE_ID);
        testEmployeeUser.setEmail("empleado@test.com");
    }

    @Test
    @DisplayName("Debería guardar una orden exitosamente si todas las validaciones pasan")
    void saveOrder_Success() {
        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        OrderPlate op2 = new OrderPlate();
        op2.setIdPlate(PLATE_ID_2);
        op2.setQuantity(2);

        Order localTestOrder = Order.builder()
                .idRestaurant(RESTAURANT_ID)
                .idClient(CLIENT_ID)
                .plates(List.of(op1, op2))
                .build();

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);

        when(platePersistencePort.getPlatesIdsByRestaurant(RESTAURANT_ID))
                .thenReturn(List.of(PLATE_ID_1, PLATE_ID_2));

        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);

        Order savedMockOrder = Order.builder().id(1L).build();
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(savedMockOrder);

        assertDoesNotThrow(() -> orderUseCase.saveOrder(localTestOrder));

        verify(platePersistencePort).getPlatesIdsByRestaurant(RESTAURANT_ID);
        verify(orderPersistencePort).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar RestaurantNotExistException si el restaurante no existe")
    void saveOrder_ThrowsRestaurantNotExistException() {
        Long nonExistentRestaurantId = 99L;

        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        Order localTestOrder = new Order();
        localTestOrder.setIdRestaurant(nonExistentRestaurantId);
        localTestOrder.setPlates(List.of(op1));


        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(nonExistentRestaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotExistException.class, () -> orderUseCase.saveOrder(localTestOrder));

        verify(orderPersistencePort, never()).hasActiveOrder(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar UserHasActiveOrderException si el cliente ya tiene una orden activa")
    void saveOrder_ThrowsUserHasActiveOrderException() {
        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        Order localTestOrder = new Order();
        localTestOrder.setIdRestaurant(RESTAURANT_ID);
        localTestOrder.setPlates(List.of(op1));

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(true);


        assertThrows(UserHasActiveOrderException.class, () -> orderUseCase.saveOrder(localTestOrder));

        verify(orderPersistencePort, times(1)).hasActiveOrder(CLIENT_ID);
        verify(platePersistencePort, never()).getPlateById(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar PlateNotAvailableException si un plato no pertenece al restaurante")
    void saveOrder_ThrowsPlateBelongsToAnotherRestaurantException() {
        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        OrderPlate op2 = new OrderPlate();
        op2.setIdPlate(PLATE_ID_2);
        op2.setQuantity(2);

        Order localTestOrder = Order.builder()
                .idRestaurant(RESTAURANT_ID)
                .plates(List.of(op1,op2))
                .build();

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));

        when(platePersistencePort.getPlatesIdsByRestaurant(RESTAURANT_ID))
                .thenReturn(List.of(999L));

        assertThrows(PlateNotAvailableException.class, () -> orderUseCase.saveOrder(localTestOrder));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería obtener la lista de órdenes por estado exitosamente para un empleado")
    void getOrdersByStatus_Success() {
        int page = 0;
        int size = 10;
        OrderStatus status = OrderStatus.PENDING;

        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(Order.builder()
                .id(10L)
                .idClient(CLIENT_ID)
                .date(LocalDateTime.now())
                .status(status)
                .idChef(1L)
                .idRestaurant(testRestaurant.getId())
                .build());

        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        when(orderPersistencePort.getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, page, size)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderUseCase.getOrdersByStatus(status, page, size);

        assertEquals(expectedOrders.size(), actualOrders.size());

        verify(restaurantEmployeePersistencePort, times(1)).getEmployee(EMPLOYEE_ID);
        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, page, size);
    }

    @Test
    @DisplayName("Debería asignar y cambiar estado a EN_PREPARACION exitosamente")
    void assignOrderAndChangeStatus_Success() {
        testOrder.setStatus(OrderStatus.PENDING);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);
        when(userGatewayPort.getUserById(EMPLOYEE_ID)).thenReturn(testEmployeeUser);

        assertDoesNotThrow(() -> orderUseCase.transitionToPreparation(ORDER_ID));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort, times(1)).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.IN_PREPARATION, savedOrder.getStatus());
        assertEquals(EMPLOYEE_ID, savedOrder.getIdChef());

        verify(userGatewayPort, times(1)).saveOrderTrace(any(Traceability.class));
    }

    @Test
    @DisplayName("Debería lanzar OrderNotFoundException si la orden no existe al asignar")
    void assignOrderAndChangeStatus_ThrowsOrderNotFound() {
        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.empty());
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID); // Se necesita para el checkOrder

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.transitionToPreparation(ORDER_ID));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar OrderNotInPendingStatusException si el estado no es PENDIENTE")
    void assignOrderAndChangeStatus_ThrowsNotInPending() {
        testOrder.setStatus(OrderStatus.READY);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        assertThrows(OrderNotInPendingStatusException.class, () -> orderUseCase.transitionToPreparation(ORDER_ID));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería notificar y cambiar estado a LISTO exitosamente")
    void notifyOrderReady_Success() {
        testOrder.setStatus(OrderStatus.IN_PREPARATION);
        testOrder.setIdChef(EMPLOYEE_ID);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);
        when(userGatewayPort.getUserById(EMPLOYEE_ID)).thenReturn(testEmployeeUser);

        assertDoesNotThrow(() -> orderUseCase.transitionToReady(ORDER_ID));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort, times(1)).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.READY, savedOrder.getStatus());
        assertNotNull(savedOrder.getSecurityPin());

        verify(userGatewayPort, times(1)).sendSms(eq(testClient.getPhoneNumber()), contains("Tu pedido está listo. Reclámalo con el PIN: "));
        verify(userGatewayPort, times(1)).saveOrderTrace(any(Traceability.class));
    }

    @Test
    @DisplayName("Debería lanzar OrderNotInPreparationStatusException si el estado no es EN_PREPARACION")
    void notifyOrderReady_ThrowsNotInPreparation() {
        testOrder.setStatus(OrderStatus.PENDING);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        assertThrows(OrderNotInPreparationStatusException.class, () -> orderUseCase.transitionToReady(ORDER_ID));

        verify(userGatewayPort, never()).sendSms(anyString(), anyString());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería cambiar estado a ENTREGADO con PIN correcto")
    void transitionToDelivered_Success() {
        testOrder.setStatus(OrderStatus.READY);
        testOrder.setSecurityPin(SECURITY_PIN);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);
        when(userGatewayPort.getUserById(EMPLOYEE_ID)).thenReturn(testEmployeeUser);

        assertDoesNotThrow(() -> orderUseCase.transitionToDelivered(ORDER_ID, SECURITY_PIN));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort, times(1)).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.DELIVERED, savedOrder.getStatus());
        verify(userGatewayPort, times(1)).saveOrderTrace(any(Traceability.class));
    }

    @Test
    @DisplayName("Debería lanzar OrderHasIncorrectPinException si el PIN es incorrecto")
    void transitionToDelivered_ThrowsIncorrectPin() {
        testOrder.setStatus(OrderStatus.READY);
        testOrder.setSecurityPin(SECURITY_PIN);
        int incorrectPin = 9999;

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        assertThrows(OrderHasIncorrectPinException.class, () -> orderUseCase.transitionToDelivered(ORDER_ID, incorrectPin));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(userGatewayPort, never()).saveOrderTrace(any(Traceability.class));
    }

    @Test
    @DisplayName("Debería lanzar OrderNotInReadyStatusException si el estado no es LISTO (aún con PIN correcto)")
    void transitionToDelivered_ThrowsNotInReady() {
        testOrder.setStatus(OrderStatus.IN_PREPARATION);
        testOrder.setSecurityPin(SECURITY_PIN);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);

        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        assertThrows(OrderNotInReadyStatusException.class, () -> orderUseCase.transitionToDelivered(ORDER_ID, SECURITY_PIN));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería cancelar la orden si está en estado PENDIENTE y es el dueño")
    void transitionToCanceled_Success() {
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setIdClient(CLIENT_ID);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);

        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);

        assertDoesNotThrow(() -> orderUseCase.transitionToCanceled(ORDER_ID));

        verify(orderPersistencePort).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar ClientIsNotOrderOwnerException si el cliente no es el dueño")
    void transitionToCanceled_ThrowsNotOwner() {
        Long anotherClientId = 999L;
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setIdClient(CLIENT_ID);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(anotherClientId);

        assertThrows(ClientIsNotOrderOwnerException.class, () -> orderUseCase.transitionToCanceled(ORDER_ID));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(userGatewayPort, never()).sendSms(anyString(), anyString());
    }

    @Test
    @DisplayName("Debería lanzar OrderNotInPendingStatusException y notificar si se intenta cancelar fuera de PENDIENTE")
    void transitionToCanceled_ThrowsNotInPendingAndNotifies() {
        testOrder.setStatus(OrderStatus.IN_PREPARATION);
        testOrder.setIdClient(CLIENT_ID);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(userGatewayPort.getUserById(CLIENT_ID)).thenReturn(testClient);

        assertThrows(OrderNotInPendingStatusException.class,
                () -> orderUseCase.transitionToCanceled(ORDER_ID));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(userGatewayPort, times(1)).sendSms(eq(testClient.getPhoneNumber()), contains("Lo sentimos, su pedido ya está en preparación y no puede cancelarse"));
    }

    @Test
    @DisplayName("Debería obtener la trazabilidad de la orden si el usuario es el dueño")
    void getTracesByOrderId_IsOwner_Success() {
        testOrder.setIdClient(CLIENT_ID);
        Traceability mockTrace = Traceability.builder()
                .orderId(ORDER_ID)
                .clientId(CLIENT_ID)
                .restaurantId(RESTAURANT_ID)
                .newStatus(OrderStatus.READY.getDbValue())
                .date(LocalDateTime.now())
                .build();

        List<Traceability> expectedTraces = List.of(mockTrace);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(userGatewayPort.getTracesByOrderId(ORDER_ID)).thenReturn(expectedTraces);

        List<Traceability> actualTraces = assertDoesNotThrow(() -> orderUseCase.getTracesByOrderId(ORDER_ID));

        assertFalse(actualTraces.isEmpty());
        verify(userGatewayPort, times(1)).getTracesByOrderId(ORDER_ID);
    }

    @Test
    @DisplayName("Debería lanzar ClientIsNotOrderOwnerException si el usuario no es el dueño al pedir trazabilidad")
    void getTracesByOrderId_IsNotOwner_Throws() {
        Long anotherUserId = 999L;
        testOrder.setIdClient(CLIENT_ID);

        when(orderPersistencePort.getOrderById(ORDER_ID)).thenReturn(Optional.of(testOrder));
        when(tokenPort.getUserId()).thenReturn(anotherUserId);

        assertThrows(ClientIsNotOrderOwnerException.class, () -> orderUseCase.getTracesByOrderId(ORDER_ID));

        verify(userGatewayPort, never()).getTracesByOrderId(anyLong());
    }

    @Test
    @DisplayName("Debería obtener el ranking de empleados si el usuario es el dueño del restaurante")
    void getEmployeePerformances_Success() {
        List<EmployeePerformance> expectedRanking = List.of(new EmployeePerformance());
        testRestaurant.setIdOwner(CLIENT_ID);

        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(userGatewayPort.getEmployeePerformance(RESTAURANT_ID)).thenReturn(expectedRanking);

        List<EmployeePerformance> actualRanking = assertDoesNotThrow(() -> orderUseCase.getEmployeePerformances(RESTAURANT_ID));

        assertFalse(actualRanking.isEmpty());
        verify(userGatewayPort, times(1)).getEmployeePerformance(RESTAURANT_ID);
    }

    @Test
    @DisplayName("Debería lanzar UserIsNotOwnerRestaurantException si no es el dueño al pedir ranking")
    void getEmployeePerformances_ThrowsNotOwner() {
        Long anotherUserId = 999L;
        testRestaurant.setIdOwner(CLIENT_ID);

        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(tokenPort.getUserId()).thenReturn(anotherUserId);

        assertThrows(UserIsNotOwnerRestaurantException.class, () -> orderUseCase.getEmployeePerformances(RESTAURANT_ID));

        verify(userGatewayPort, never()).getEmployeePerformance(anyLong());
    }

    @Test
    @DisplayName("Debería obtener métricas de eficiencia si el usuario es el dueño del restaurante")
    void getOrderMetrics_Success() {
        List<OrderEfficiency> expectedMetrics = List.of(new OrderEfficiency());
        testRestaurant.setIdOwner(CLIENT_ID);

        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(userGatewayPort.getOrderEfficiency(RESTAURANT_ID)).thenReturn(expectedMetrics);

        List<OrderEfficiency> actualMetrics = assertDoesNotThrow(() -> orderUseCase.getOrderMetrics(RESTAURANT_ID));

        assertFalse(actualMetrics.isEmpty());
        verify(userGatewayPort, times(1)).getOrderEfficiency(RESTAURANT_ID);
    }

}