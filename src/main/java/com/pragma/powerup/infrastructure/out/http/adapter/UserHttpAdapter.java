package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.out.http.UserResponseDto;
import com.pragma.powerup.infrastructure.out.http.feign.IUserFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHttpAdapter implements IUserGatewayPort {

    private final IUserFeignClient userFeignClient;

    @Override
    public boolean isUserOwner(Long userId) {
        try {
            UserResponseDto userResponse = userFeignClient.getUserById(userId);

            return userResponse != null
                    && userResponse.getRole() != null
                    && "PROPIETARIO".equals(userResponse.getRole().getName());

        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
