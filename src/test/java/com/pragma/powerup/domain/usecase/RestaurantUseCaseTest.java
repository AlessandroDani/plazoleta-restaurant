package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void saveRestaurant_Success() {
        when(restaurantPersistence.getRestaurantByNit(validRestaurant.getNit())).thenReturn(null);


        assertDoesNotThrow(() -> restaurantUseCase.saveRestaurant(validRestaurant));


        verify(restaurantPersistence).getRestaurantByNit(validRestaurant.getNit());
        verify(userGateway).isUserOwner(validRestaurant.getIdOwner());
        verify(restaurantPersistence).saveRestaurant(validRestaurant);
    }

    @Test
    void saveRestaurant_UserServiceError_ThrowsException() {
        when(restaurantPersistence.getRestaurantByNit(validRestaurant.getNit())).thenReturn(new Restaurant());

        assertThrows(RestaurantAlreadyExistException.class,
                () -> restaurantUseCase.saveRestaurant(validRestaurant));

        verify(userGateway, never()).isUserOwner(anyLong());
        verify(restaurantPersistence, never()).saveRestaurant(any(Restaurant.class));
    }
}
