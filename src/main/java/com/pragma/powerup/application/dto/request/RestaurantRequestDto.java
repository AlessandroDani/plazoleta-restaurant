package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class RestaurantRequestDto {
    private BigInteger id;
    private String name;
    private Long nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    //private User idPropietario;
}
