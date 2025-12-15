package com.pragma.powerup.infrastructure.out.http.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePerformanceResponseDto {
    private Long employeeId;
    private Double averageDurationInMinutes;
}