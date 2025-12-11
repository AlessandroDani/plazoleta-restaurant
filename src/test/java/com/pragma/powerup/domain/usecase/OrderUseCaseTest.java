package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.PlateBelongsToAnotherRestaurantException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserHasActiveOrderException;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private OrderUseCase orderUseCase;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long CLIENT_ID = 200L;
    private static final Long PLATE_ID_1 = 10L;
    private static final Long PLATE_ID_2 = 11L;

    private Order testOrder;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(RESTAURANT_ID);
        testRestaurant.setIdOwner(1L);
        List<OrderPlate> orderPlates = List.of(
                new OrderPlate(1L, PLATE_ID_1, 2L, 4),
                new OrderPlate(2L, PLATE_ID_2, 1L, 2)
        );

        testOrder = new Order();
        testOrder.setIdRestaurant(RESTAURANT_ID);
        testOrder.setPlates(orderPlates);
    }


    @Test
    @DisplayName("Debería guardar una orden exitosamente si todas las validaciones pasan")
    void saveOrder_Success() {
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);

        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);
        Plate plate2 = new Plate();
        plate2.setIdRestaurant(RESTAURANT_ID);

        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.of(plate2));

        assertDoesNotThrow(() -> orderUseCase.saveOrder(testOrder));

        verify(orderPersistencePort, times(1)).hasActiveOrder(CLIENT_ID);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);
        verify(orderPersistencePort, times(1)).saveOrder(testOrder);

        assertEquals(CLIENT_ID, testOrder.getIdClient());
        assertEquals(OrderStatus.PENDING, testOrder.getStatus());
        assertEquals(LocalDate.now(), testOrder.getDate());
    }

    @Test
    @DisplayName("Debería lanzar RestaurantNotExistException si el restaurante no existe")
    void saveOrder_ThrowsRestaurantNotExistException() {
        Long nonExistentRestaurantId = 99L;
        testOrder.setIdRestaurant(nonExistentRestaurantId);

        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(nonExistentRestaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotExistException.class, () -> orderUseCase.saveOrder(testOrder));

        verify(orderPersistencePort, never()).hasActiveOrder(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar UserHasActiveOrderException si el cliente ya tiene una orden activa")
    void saveOrder_ThrowsUserHasActiveOrderException() {
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));

        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(true);


        assertThrows(UserHasActiveOrderException.class, () -> orderUseCase.saveOrder(testOrder));

        verify(orderPersistencePort, times(1)).hasActiveOrder(CLIENT_ID);
        verify(platePersistencePort, never()).getPlateById(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    @DisplayName("Debería lanzar PlateNotFoundException si alguno de los platos no existe")
    void saveOrder_ThrowsPlateNotFoundException() {
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);


        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);
        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.empty());


        assertThrows(PlateNotFoundException.class, () -> orderUseCase.saveOrder(testOrder));


        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);
    }

    @Test
    @DisplayName("Debería lanzar PlateBelongsToAnotherRestaurantException si un plato no pertenece al restaurante")
    void saveOrder_ThrowsPlateBelongsToAnotherRestaurantException() {
        when(tokenPort.getUserId()).thenReturn(CLIENT_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(orderPersistencePort.hasActiveOrder(CLIENT_ID)).thenReturn(false);


        Plate plate1 = new Plate();
        plate1.setIdRestaurant(RESTAURANT_ID);
        Plate plate2 = new Plate();
        plate2.setIdRestaurant(999L);

        when(platePersistencePort.getPlateById(PLATE_ID_1)).thenReturn(Optional.of(plate1));
        when(platePersistencePort.getPlateById(PLATE_ID_2)).thenReturn(Optional.of(plate2));


        assertThrows(PlateBelongsToAnotherRestaurantException.class, () -> orderUseCase.saveOrder(testOrder));

        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_1);
        verify(platePersistencePort, times(1)).getPlateById(PLATE_ID_2);
    }
}