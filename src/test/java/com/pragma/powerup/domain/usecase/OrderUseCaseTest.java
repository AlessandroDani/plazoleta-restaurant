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

    @InjectMocks
    private OrderUseCase orderUseCase;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long CLIENT_ID = 200L;
    private static final Long EMPLOYEE_ID = 300L;
    private static final Long PLATE_ID_1 = 10L;
    private static final Long PLATE_ID_2 = 11L;

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

        Order localTestOrder = new Order();
        localTestOrder.setIdRestaurant(RESTAURANT_ID);
        localTestOrder.setPlates(List.of(op1, op2));

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);

        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);
        Plate plate2 = new Plate();
        plate2.setIdRestaurant(RESTAURANT_ID);
        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.of(plate2));

        assertDoesNotThrow(() -> orderUseCase.saveOrder(localTestOrder));

        verify(orderPersistencePort, times(1)).hasActiveOrder(CLIENT_ID);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort, times(1)).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(CLIENT_ID, savedOrder.getIdClient());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        assertTrue(savedOrder.getDate().isAfter(LocalDateTime.now().minusSeconds(1)));
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
    @DisplayName("Debería lanzar PlateNotFoundException si alguno de los platos no existe")
    void saveOrder_ThrowsPlateNotFoundException() {
        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        OrderPlate op2 = new OrderPlate();
        op2.setIdPlate(PLATE_ID_2);
        op2.setQuantity(2);

        Order localTestOrder = new Order();
        localTestOrder.setIdRestaurant(RESTAURANT_ID);
        localTestOrder.setPlates(List.of(op1, op2));

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);


        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);

        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.empty());


        assertThrows(PlateNotFoundException.class, () -> orderUseCase.saveOrder(localTestOrder));


        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);
    }

    @Test
    @DisplayName("Debería lanzar PlateBelongsToAnotherRestaurantException si un plato no pertenece al restaurante")
    void saveOrder_ThrowsPlateBelongsToAnotherRestaurantException() {
        OrderPlate op1 = new OrderPlate();
        op1.setIdPlate(PLATE_ID_1);
        op1.setQuantity(4);

        OrderPlate op2 = new OrderPlate();
        op2.setIdPlate(PLATE_ID_2);
        op2.setQuantity(2);

        Order localTestOrder = new Order();
        localTestOrder.setIdRestaurant(RESTAURANT_ID);
        localTestOrder.setPlates(List.of(op1, op2));

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);

        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);
        Plate plate2 = new Plate();
        plate2.setIdRestaurant(999L);

        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.of(plate2));

        assertThrows(PlateBelongsToAnotherRestaurantException.class, () -> orderUseCase.saveOrder(localTestOrder));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);
    }

    @Test
    @DisplayName("Debería obtener la lista de órdenes por estado exitosamente para un empleado")
    void getOrdersByStatus_Success() {
        int page = 0;
        int size = 10;
        OrderStatus status = OrderStatus.PENDING;

        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(new Order(10L, CLIENT_ID, LocalDateTime.now(), status, testRestaurant.getId(), null, null));

        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        when(orderPersistencePort.getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, page, size)).thenReturn(Optional.of(expectedOrders));

        List<Order> actualOrders = orderUseCase.getOrdersByStatus(status, page, size);

        assertEquals(expectedOrders.size(), actualOrders.size());

        verify(restaurantEmployeePersistencePort, times(1)).getEmployee(EMPLOYEE_ID);
        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, page, size);
    }

    @Test
    @DisplayName("Debería lanzar OrderNotFoundException si no hay órdenes para el estado y restaurante")
    void getOrdersByStatus_ThrowsOrderNotFoundException() {
        OrderStatus status = OrderStatus.IN_PREPARATION;

        when(tokenPort.getUserId()).thenReturn(EMPLOYEE_ID);
        when(restaurantEmployeePersistencePort.getEmployee(EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));
        when(orderPersistencePort.getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, 0, 10)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.getOrdersByStatus(status, 0, 10));

        verify(orderPersistencePort, times(1)).getOrdersByRestaurantAndStatus(RESTAURANT_ID, status, 0, 10);
    }
}