package com.pragma.powerup.infrastructure.out.http.mapper;

import com.pragma.powerup.domain.model.EmployeePerformance;
import com.pragma.powerup.domain.model.OrderEfficiency;

import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.infrastructure.out.http.request.TraceabilityRequestDto;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityRequestMapper {

    TraceabilityRequestDto toRequestDto(Traceability traceability);
    EmployeePerformance toRequestEmployeeDto(EmployeePerformanceResponseDto employeePerformanceResponseDto);
    OrderEfficiency toRequestOrderDto(OrderEfficiencyResponseDto orderEfficiencyResponseDto);
}
