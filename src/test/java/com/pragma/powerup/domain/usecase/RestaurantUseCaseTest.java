package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistence;

    @Mock
    private IUserGatewayPort userGateway;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;
    private Restaurant validRestaurant;


    @BeforeEach
    void setUp() {
        validRestaurant = new Restaurant(
                1L,
                "RestaurantTest",
                "123456",
                "address",
                "310",
                "logo.png",
                5L);
    }

    @Test
    @DisplayName("Debería guardar el restaurante si el NIT no existe y el usuario tiene rol de Propietario")
    void saveRestaurant_Success() {
        when(restaurantPersistence.existsRestaurantByNit(validRestaurant.getNit())).thenReturn(false);

        assertDoesNotThrow(() -> restaurantUseCase.saveRestaurant(validRestaurant));

        verify(restaurantPersistence).existsRestaurantByNit(validRestaurant.getNit());
        verify(userGateway).isUserOwner(validRestaurant.getIdOwner());
        verify(restaurantPersistence).saveRestaurant(validRestaurant);
    }

    @Test
    @DisplayName("Debería lanzar RestaurantAlreadyExistException si ya existe un restaurante con el mismo NIT")
    void saveRestaurant_ThrowsRestaurantAlreadyExistException() {
        when(restaurantPersistence.existsRestaurantByNit(validRestaurant.getNit())).thenReturn(true);

        assertThrows(RestaurantAlreadyExistException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));

        verify(restaurantPersistence).existsRestaurantByNit(validRestaurant.getNit());
        verify(userGateway, never()).isUserOwner(anyLong());
        verify(restaurantPersistence, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Debería propagar la excepción si el UserGateway falla al validar el rol de Propietario")
    void saveRestaurant_ThrowsExceptionIfOwnerRoleIsInvalid() {
        when(restaurantPersistence.existsRestaurantByNit(validRestaurant.getNit())).thenReturn(false);

        doThrow(new RuntimeException("El usuario no tiene el rol de propietario requerido")).when(userGateway).isUserOwner(validRestaurant.getIdOwner());

        assertThrows(RuntimeException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));

        verify(restaurantPersistence).existsRestaurantByNit(validRestaurant.getNit());
        verify(userGateway).isUserOwner(validRestaurant.getIdOwner());
        verify(restaurantPersistence, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    @DisplayName("Debería retornar la lista de restaurantes paginada y ordenada correctamente")
    void getAllRestaurant_Success() {
        int page = 0;
        int size = 5;

        List<Restaurant> mockList = List.of(
                new Restaurant(2L, "Zeta", "777", "addr1", "311", "logo.png", 9L),
                new Restaurant(3L, "Alfa", "888", "addr2", "312", "logo.png", 9L)
        );

        when(restaurantPersistence.getAllRestaurant(page, size)).thenReturn(mockList);

        List<Restaurant> result = assertDoesNotThrow(
                () -> restaurantUseCase.getAllRestaurant(page, size),
                "No debería lanzar excepción cuando se encuentran restaurantes."
        );

        verify(restaurantPersistence).getAllRestaurant(page, size);
        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals(2, result.size());
    }
}
