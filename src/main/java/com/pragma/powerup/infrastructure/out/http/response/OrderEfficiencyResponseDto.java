package com.pragma.powerup.infrastructure.out.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEfficiencyResponseDto {
    private Long orderId;
    private Double durationInMinutes;
}
