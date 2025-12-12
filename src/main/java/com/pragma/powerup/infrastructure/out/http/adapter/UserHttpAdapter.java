package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.RoleNotFoundException;
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
    public void isUserOwner(Long userId) {
        search(userId, "PROPIETARIO");
    }

    @Override
    public void isUserEmployee(Long userId) {
        search(userId, "EMPLEADO");
    }

    private void search(Long userId, String role){
        try {
            UserResponseDto userResponse = userFeignClient.getUserById(userId);
            if (userResponse.getRole() == null || !role.equals(userResponse.getRole().getName())) {
                throw new InvalidRoleException();
            }
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }
}