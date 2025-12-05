package com.pragma.powerup.domain.spi;

public interface IUserGatewayPort {
    void isUserOwner(Long userId);
}