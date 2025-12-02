package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.OwnerNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
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
            if (userResponse.getRole() == null || !"PROPIETARIO".equals(userResponse.getRole().getName())) {
                throw new InvalidRoleException();
            }
            return true;
        } catch (FeignException.NotFound e) {
            throw new OwnerNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }
}
