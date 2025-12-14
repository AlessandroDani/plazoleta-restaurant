package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.RoleNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
import com.pragma.powerup.infrastructure.out.http.feign.ISmsFeignClient;
import com.pragma.powerup.infrastructure.out.http.feign.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.http.mapper.IUserRequestMapper;
import com.pragma.powerup.infrastructure.out.http.request.SmsRequestDto;
import com.pragma.powerup.infrastructure.out.http.response.UserResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHttpAdapter implements IUserGatewayPort {

    private final IUserFeignClient userFeignClient;
    private final ISmsFeignClient smsFeignClient;
    private final IUserRequestMapper userRequestMapper;

    @Override
    public void isUserOwner(Long userId) {
        checkRole(userId, "PROPIETARIO");
    }

    @Override
    public void isUserEmployee(Long userId) {
        checkRole(userId, "EMPLEADO");
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        SmsRequestDto request = new SmsRequestDto();
        request.setPhoneNumber(phoneNumber);
        request.setMessage(message);

        try {
            smsFeignClient.sendSms(request);
        } catch (FeignException.NotFound e) {
            throw new InvalidRoleException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }

    @Override
    public User getUserById(Long clientId) {
        try {
            UserResponseDto userResponse = userFeignClient.getUserById(clientId);
            return userRequestMapper.toModel(userResponse);
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }

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