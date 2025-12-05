package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlateUseCaseTest {

    @Mock
    private IPlatePersistencePort platePersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private PlateUseCase plateUseCase;

    private Plate testPlate;
    private Plate testUpdatePlate;
    private Restaurant mockRestaurant;

    private final Long restaurantOwnerId = 5L;
    private final Long otherUserId = 6L;
    private final Long restaurantId = 1L;
    private final Long plateId = 10L;


    @BeforeEach
    void setUp() {
        testPlate = new Plate(
                null,
                "Arepa con todo",
                1L,
                "Plato típico",
                15000L,
                restaurantId,
                "http://img.com/arepa.png",
                false
        );

        testUpdatePlate = new Plate(
                plateId,
                "Arepa Vieja",
                1L,
                "Descripción vieja",
                10000L,
                restaurantId,
                "http://img.com/arepa.png",
                true
        );

        mockRestaurant = new Restaurant();
        mockRestaurant.setId(restaurantId);
        mockRestaurant.setIdOwner(restaurantOwnerId);
        mockRestaurant.setName("Arepas Don Juan");
    }

//    @Test
//    void savePlate_Success_IsOwner() {
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//        assertDoesNotThrow(() -> plateUseCase.savePlate(testPlate, restaurantOwnerId));
//
//        verify(platePersistencePort, times(1)).savePlate(any(Plate.class));
//    }
//
//    @Test
//    void savePlate_ThrowsException_UserIsNotOwner() {
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//        assertThrows(UserIsNotOwnerRestaurantException.class, () ->
//                plateUseCase.savePlate(testPlate, otherUserId)
//        );
//
//        verify(platePersistencePort, never()).savePlate(any(Plate.class));
//    }
//
//    @Test
//    void savePlate_ThrowsException_RestaurantNotFound() {
//        when(restaurantPersistencePort.getRestaurantById(anyLong()))
//                .thenThrow(new RestaurantNotExistException());
//
//        assertThrows(RestaurantNotExistException.class, () ->
//                plateUseCase.savePlate(testPlate, restaurantOwnerId)
//        );
//
//        verify(platePersistencePort, never()).savePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_Success_UpdatePriceOnly() {
//        Long newPrice = 20000L;
//
//
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(testUpdatePlate);
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//
//        assertDoesNotThrow(() -> plateUseCase.updatePlate(
//                plateId,
//                newPrice,
//                null,
//                restaurantOwnerId));
//
//        verify(platePersistencePort, times(1)).updatePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_Success_UpdateDescriptionOnly() {
//        String newDescription = "Nueva y mejor descripción";
//
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(testUpdatePlate);
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//        assertDoesNotThrow(() -> plateUseCase.updatePlate(
//                plateId,
//                null,
//                newDescription,
//                restaurantOwnerId));
//
//        verify(platePersistencePort, times(1)).updatePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_Success_UpdateBoth() {
//        Long newPrice = 30000L;
//        String newDescription = "Descripción completa nueva";
//
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(testUpdatePlate);
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//        assertDoesNotThrow(() -> plateUseCase.updatePlate(
//                plateId,
//                newPrice,
//                newDescription,
//                restaurantOwnerId));
//
//        verify(platePersistencePort, times(1)).updatePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_ThrowsException_PlateNotFound() {
//
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(PlateNotFoundException.class, () ->
//                plateUseCase.updatePlate(plateId, 20000L, "desc", restaurantOwnerId));
//
//        verify(restaurantPersistencePort, never()).getRestaurantById(anyLong());
//        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_ThrowsException_UserIsNotOwner() {
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(testUpdatePlate);
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);
//
//        assertThrows(UserIsNotOwnerRestaurantException.class, () ->
//                plateUseCase.updatePlate(plateId, 20000L, "desc", otherUserId));
//
//        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
//    }
//
//    @Test
//    void updatePlate_ThrowsException_RestaurantNotFound() {
//        when(platePersistencePort.getPlateById(plateId)).thenReturn(testUpdatePlate);
//        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(null);
//
//
//        assertThrows(RestaurantNotExistException.class, () ->
//                plateUseCase.updatePlate(plateId, 20000L, "desc", restaurantOwnerId));
//
//        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
//    }

}