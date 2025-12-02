package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserGatewayPort userGatewayPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant testRestaurant;
    private Restaurant existingRestaurant;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setName("El Rincón Criollo");
        testRestaurant.setNit("123456789");
        testRestaurant.setAddress("Calle 10");
        testRestaurant.setPhoneNumber("+573001234567");
        testRestaurant.setUrlLogo("http://logo.com/rc.png");
        testRestaurant.setIdOwner(2L);

        existingRestaurant = new Restaurant();
        existingRestaurant.setName("Restaurante Existente");
        existingRestaurant.setNit("987654321");
        existingRestaurant.setAddress("Av. Principal");
        existingRestaurant.setPhoneNumber("+573009876543");
        existingRestaurant.setUrlLogo("http://logo.com/re.png");
        existingRestaurant.setIdOwner(2L);
    }
}
