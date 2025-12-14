package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.spi.*;
import com.pragma.powerup.domain.usecase.OrderUseCase;
import com.pragma.powerup.domain.usecase.PlateUseCase;
import com.pragma.powerup.domain.usecase.RestaurantEmployeeUseCase;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import com.pragma.powerup.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.PlateJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestaurantEmployeeAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlateEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlateRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IPlateRepository plateRepository;
    private final IPlateEntityMapper plateEntityMapper;
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    @Bean
    public IRestaurantServicePort  restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort, IUserGatewayPort  userGatewayPort) {
        return new RestaurantUseCase(restaurantPersistencePort, userGatewayPort);
    }

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IPlateServicePort plateServicePort(IPlatePersistencePort platePersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, ICategoryPersistencePort  categoryPersistencePort) {
        return new PlateUseCase(platePersistencePort, restaurantPersistencePort, tokenPort, categoryPersistencePort);
    }

    @Bean
    public IPlatePersistencePort platePersistencePort() {
        return new PlateJpaAdapter(plateRepository, plateEntityMapper, restaurantRepository);
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IPlatePersistencePort platePersistencePort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserGatewayPort userGatewayPort) {
        return new OrderUseCase(orderPersistencePort, restaurantPersistencePort, tokenPort, platePersistencePort, restaurantEmployeePersistencePort, userGatewayPort);

    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public IRestaurantEmployeeServicePort restaurantEmployeeServicePort(IRestaurantPersistencePort restaurantPersistencePort, ITokenPort tokenPort, IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort, IUserGatewayPort userGatewayPort) {
        return new RestaurantEmployeeUseCase(restaurantEmployeePersistencePort ,restaurantPersistencePort, tokenPort, userGatewayPort);
    }

    @Bean
    public IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort() {
        return new RestaurantEmployeeAdapter(restaurantEmployeeRepository, restaurantEmployeeEntityMapper);
    }


}
