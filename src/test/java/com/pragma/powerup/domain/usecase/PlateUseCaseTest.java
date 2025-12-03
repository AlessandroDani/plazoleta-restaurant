package com.pragma.powerup.domain.usecase;

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
    private Restaurant mockRestaurant;
    private final Long restaurantOwnerId = 5L;
    private final Long otherUserId = 6L;
    private final Long restaurantId = 1L;

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

        mockRestaurant = new Restaurant();
        mockRestaurant.setId(restaurantId);
        mockRestaurant.setIdOwner(restaurantOwnerId);
        mockRestaurant.setName("Arepas Don Juan");
    }

    @Test
    void savePlate_Success_IsOwner() {
        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);

        assertDoesNotThrow(() -> plateUseCase.savePlate(testPlate, restaurantOwnerId));

        verify(platePersistencePort, times(1)).savePlate(any(Plate.class));
    }

    @Test
    void savePlate_ThrowsException_UserIsNotOwner() {
        when(restaurantPersistencePort.getRestaurantById(restaurantId)).thenReturn(mockRestaurant);

        assertThrows(UserIsNotOwnerRestaurantException.class, () ->
                plateUseCase.savePlate(testPlate, otherUserId)
        );

        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    void savePlate_ThrowsException_RestaurantNotFound() {
        when(restaurantPersistencePort.getRestaurantById(anyLong()))
                .thenThrow(new RestaurantNotExistException());

        assertThrows(RestaurantNotExistException.class, () ->
                plateUseCase.savePlate(testPlate, restaurantOwnerId)
        );

        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }
}