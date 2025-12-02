package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    private Long idOwner;
}
