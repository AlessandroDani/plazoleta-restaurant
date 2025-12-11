package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlateUseCaseTest {

    @Mock
    private IPlatePersistencePort platePersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private ITokenPort tokenPort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private PlateUseCase plateUseCase;


    private static final Long RESTAURANT_ID = 1L;
    private static final Long OWNER_ID = 100L;
    private static final Long OTHER_USER_ID = 200L;
    private static final Long VALID_CATEGORY_ID = 1L;
    private Plate testPlate;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testPlate = new Plate(
                null,
                "Arepa con todo",
                VALID_CATEGORY_ID,
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
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.existsPlateByName(anyString())).thenReturn(false);
        when(categoryPersistencePort.existsCategoryById(VALID_CATEGORY_ID)).thenReturn(true);

        plateUseCase.savePlate(testPlate);

        verify(platePersistencePort, times(1)).existsPlateByName(testPlate.getName());
        verify(platePersistencePort).savePlate(testPlate);
        assertTrue(testPlate.isActive());
    }

    @Test
    @DisplayName("Debería lanzar PlateAlreadyExistException si el plato ya existe (primera validación)")
    void savePlate_ThrowsPlateAlreadyExistException() {

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(categoryPersistencePort.existsCategoryById(VALID_CATEGORY_ID)).thenReturn(true);
        when(platePersistencePort.existsPlateByName(anyString())).thenReturn(true);



        assertThrows(PlateAlreadyExistException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar RestaurantNotExistException si el restaurante no existe")
    void savePlate_ThrowsRestaurantNotExistException() {
        when(tokenPort.getUserId()).thenReturn(OWNER_ID);

        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotExistException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).existsPlateByName(anyString());
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar UserIsNotOwnerRestaurantException si el usuario no es propietario")
    void savePlate_ThrowsUserIsNotOwnerRestaurantException() {
        when(tokenPort.getUserId()).thenReturn(OTHER_USER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));


        assertThrows(UserIsNotOwnerRestaurantException.class, () -> plateUseCase.savePlate(testPlate));
        verify(platePersistencePort, never()).existsPlateByName(anyString());
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar CategoryNotFoundException si la categoría del plato no existe")
    void savePlate_ThrowsCategoryNotFoundException() {
        Long invalidCategoryId = 999L;
        Long originalCategoryId = testPlate.getIdCategory();
        testPlate.setIdCategory(invalidCategoryId);

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));

        when(categoryPersistencePort.existsCategoryById(invalidCategoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> plateUseCase.savePlate(testPlate));

        verify(categoryPersistencePort, times(1)).existsCategoryById(invalidCategoryId);
        verify(platePersistencePort, never()).existsPlateByName(anyString());
        verify(platePersistencePort, never()).savePlate(any(Plate.class));
        testPlate.setIdCategory(originalCategoryId);

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
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

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
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

        plateUseCase.updatePlate(newPrice, null, plateId);

        verify(platePersistencePort).updatePlate(existingPlate);
        assertEquals(newPrice, existingPlate.getPrice());
        assertEquals(oldDescription, existingPlate.getDescription());
    }

    @Test
    @DisplayName("Debería lanzar PlateNotFoundException si el plato a actualizar no existe")
    void updatePlate_ThrowsPlateNotFoundException() {

        Long plateId = 5L;
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.empty());

        assertThrows(PlateNotFoundException.class, () -> plateUseCase.updatePlate(10000L, "Desc", plateId));
        verify(restaurantPersistencePort, never()).getRestaurantById(anyLong());
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
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

        assertThrows(UserIsNotOwnerRestaurantException.class, () -> plateUseCase.updatePlate(10000L, "Desc", plateId));
        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería habilitar el plato y llamar al puerto de persistencia")
    void updateActivePlate_EnablePlate_Success() {
        Long plateId = 5L;
        boolean newStatus = true;

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);
        existingPlate.setActive(false);

        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

        assertDoesNotThrow(() -> plateUseCase.updateActivePlate(newStatus, plateId));

        assertTrue(existingPlate.isActive());
        verify(platePersistencePort).updatePlate(existingPlate);
    }

    @Test
    @DisplayName("Debería deshabilitar el plato y llamar al puerto de persistencia")
    void updateActivePlate_DisablePlate_Success() {
        Long plateId = 6L;
        boolean newStatus = false;

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);
        existingPlate.setActive(true);


        when(tokenPort.getUserId()).thenReturn(OWNER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

        assertDoesNotThrow(() -> plateUseCase.updateActivePlate(newStatus, plateId));

        assertFalse(existingPlate.isActive());
        verify(platePersistencePort).updatePlate(existingPlate);
    }

    @Test
    @DisplayName("Debería lanzar PlateNotFoundException si el plato a cambiar estado no existe")
    void updateActivePlate_ThrowsPlateNotFoundException() {
        Long nonExistentPlateId = 99L;
        boolean newStatus = true;


        when(platePersistencePort.getPlateById(nonExistentPlateId)).thenReturn(Optional.empty());

        assertThrows(PlateNotFoundException.class,
                () -> plateUseCase.updateActivePlate(newStatus, nonExistentPlateId));

        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
    }

    @Test
    @DisplayName("Debería lanzar UserIsNotOwnerRestaurantException si un usuario que no es propietario intenta cambiar el estado")
    void updateActivePlate_ThrowsUserIsNotOwnerRestaurantException() {
        Long plateId = 5L;
        boolean newStatus = false;

        Plate existingPlate = new Plate();
        existingPlate.setId(plateId);
        existingPlate.setIdRestaurant(RESTAURANT_ID);
        existingPlate.setActive(true);

        when(tokenPort.getUserId()).thenReturn(OTHER_USER_ID);
        when(restaurantPersistencePort.getRestaurantById(RESTAURANT_ID)).thenReturn(Optional.of(testRestaurant));
        when(platePersistencePort.getPlateById(plateId)).thenReturn(Optional.of(existingPlate));

        assertThrows(UserIsNotOwnerRestaurantException.class,
                () -> plateUseCase.updateActivePlate(newStatus, plateId));

        assertTrue(existingPlate.isActive());
        verify(platePersistencePort, never()).updatePlate(any(Plate.class));
    }

}