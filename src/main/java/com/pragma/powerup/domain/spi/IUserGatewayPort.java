package com.pragma.powerup.domain.spi;

public interface IUserGatewayPort {
    boolean isUserOwner(Long userId);
}
