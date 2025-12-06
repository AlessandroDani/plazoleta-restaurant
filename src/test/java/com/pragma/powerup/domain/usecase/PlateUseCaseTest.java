package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.PlateAlreadyExistException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlateUseCaseTest {

    @Mock
    private IPlatePersistencePort platePersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ITokenPort tokenPort;

    @InjectMocks
    private PlateUseCase plateUseCase;

    private final Long RESTAURANT_ID = 1L;
    private final Long OWNER_ID = 100L;
    private final Long OTHER_USER_ID = 200L;
    private Plate testPlate;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testPlate = new Plate(
                null,
                "Arepa con todo",
                1L,
                "Plato típico",
                15000L,
                RESTAURANT_ID,
                "http://img.com/arepa.png",
                false
        );

        testRestaurant = new Restaurant();
        testRestaurant.setId(RESTAURANT_ID);
        testRestaurant.setIdOwner(OWNER_ID);
        testRestaurant.setName("Arepas Don Juan");
    }

    @Test
    @DisplayName("Debería guardar un plato si no existe y el usuario es el propietario")
    void savePlate_Success() {
        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(testRestaurant);

        when(platePersistencePort.getPlateByName(anyString())).thenReturn(null);

        plateUseCase.savePlate(testPlate);

        verify(platePersistencePort, times(2)).getPlateByName(testPlate.getName());

        verify(platePersistencePort).savePlate(testPlate);
        assertTrue(testPlate.isActive());
    }

    @Test
    @DisplayName("Debería lanzar PlateAlreadyExistException si el plato ya existe (primera validación)")
    void savePlate_ThrowsPlateAlreadyExistException() {

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);

        when(platePersistencePort.getPlateByName(anyString())).thenReturn(new Plate());


        assertThrows(PlateAlreadyExistException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
        verify(restaurantPersistencePort, never()).getRestaurantById(anyLong());
    }

    @Test
    @DisplayName("Debería lanzar RestaurantNotExistException si el restaurante no existe")
    void savePlate_ThrowsRestaurantNotExistException() {
        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(platePersistencePort.getPlateByName(anyString())).thenReturn(null);

        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(null);

        assertThrows(RestaurantNotExistException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar UserIsNotOwnerRestaurantException si el usuario no es propietario")
    void savePlate_ThrowsUserIsNotOwnerRestaurantException() {
        when(tokenPort.getUserId()).thenReturn(OTHER_USER_ID);
        when(platePersistencePort.getPlateByName(anyString())).thenReturn(null);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(testRestaurant);

        assertThrows(UserIsNotOwnerRestaurantException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería actualizar el precio y la descripción de un plato")
    void updatePlate_UpdateAllFields_Success() {
        Long plateId = 5L;
        Long newPrice = 15000L;
        String newDescription = "Nueva descripción actualizada";

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);
        existingPlate.setPrice(10000L);
        existingPlate.setDescription("Vieja descripción");

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(platePersistencePort.getPlateById(plateId)).thenReturn(existingPlate);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(testRestaurant);

        plateUseCase.updatePlate(newPrice, newDescription, plateId);

        verify(platePersistencePort).updatePlate(existingPlate);
        assertEquals(newPrice, existingPlate.getPrice());
        assertEquals(newDescription, existingPlate.getDescription());
    }

    @Test
    @DisplayName("Debería actualizar solo el precio si la descripción es null")
    void updatePlate_UpdateOnlyPrice_Success() {
        Long plateId = 5L;
        Long newPrice = 15000L;
        String oldDescription = "Vieja descripción";

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);
        existingPlate.setPrice(10000L);
        existingPlate.setDescription(oldDescription);

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(platePersistencePort.getPlateById(plateId)).thenReturn(existingPlate);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(testRestaurant);

        plateUseCase.updatePlate(newPrice, null, plateId);

        verify(platePersistencePort).updatePlate(existingPlate);
        assertEquals(newPrice, existingPlate.getPrice());
        assertEquals(oldDescription, existingPlate.getDescription());
    }

    @Test
    @DisplayName("Debería lanzar PlateNotFoundException si el plato a actualizar no existe")
    void updatePlate_ThrowsPlateNotFoundException() {

        Long plateId = 5L;
        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(platePersistencePort.getPlateById(plateId)).thenReturn(null);

        assertThrows(PlateNotFoundException.class, () -> plateUseCase.updatePlate(10000L, "Desc", plateId));
        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar UserIsNotOwnerRestaurantException si otro usuario intenta actualizar el plato")
    void updatePlate_ThrowsUserIsNotOwnerRestaurantException() {
        Long plateId = 5L;
        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);

        when(tokenPort.getUserId()).thenReturn(OTHER_USER_ID);
        when(platePersistencePort.getPlateById(plateId)).thenReturn(existingPlate);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(testRestaurant);

        assertThrows(UserIsNotOwnerRestaurantException.class, () -> plateUseCase.updatePlate(10000L, "Desc", plateId));
        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
    }



}