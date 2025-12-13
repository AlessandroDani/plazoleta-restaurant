package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
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
        checkRole(userId, "PROPIETARIO");
    }

    @Override
    public void isUserEmployee(Long userId) {
        checkRole(userId, "EMPLEADO");
    }

    private void checkRole(Long userId, String role) {
        try {
            userFeignClient.checkRole(userId, role);
        } catch (FeignException.NotFound e) {
            throw new InvalidRoleException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }
}