package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.User;

public interface IUserGatewayPort {
    void isUserOwner(Long userId);
    void isUserEmployee(Long userId);
    void sendSms(String phoneNumber, String message);
    User getUserById(Long clientId);
}