package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserGatewayPort userGatewayPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setName("El Rincón Criollo");
        testRestaurant.setNit("123456789");
        testRestaurant.setAddress("Calle 10");
        testRestaurant.setPhoneNumber("+573001234567");
        testRestaurant.setUrlLogo("http://logo.com/rc.png");
        testRestaurant.setIdOwner(2L);
    }

    @Test
    void saveRestaurant_Success() {
        when(userGatewayPort.isUserOwner(anyLong())).thenReturn(true);

        when(restaurantPersistencePort.getRestaurantByNit(anyString())).thenReturn(null);

        assertDoesNotThrow(() -> restaurantUseCase.saveRestaurant(testRestaurant));

        verify(restaurantPersistencePort, times(1)).saveRestaurant(testRestaurant);
    }

    @Test
    void saveRestaurant_UserServiceError_ThrowsException() {
        when(userGatewayPort.isUserOwner(anyLong()))
                .thenThrow(new UserServiceCommunicationException());

        assertThrows(UserServiceCommunicationException.class, () ->
                restaurantUseCase.saveRestaurant(testRestaurant)
        );

        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }
}
