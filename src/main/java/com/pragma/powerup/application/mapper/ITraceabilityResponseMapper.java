package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.EmployeePerformance;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityResponseMapper {
    TraceabilityResponseDto toResponseDto(Traceability traceability);
    List<TraceabilityResponseDto> toResponseDtoList(List<Traceability> traceabilityList);

    OrderEfficiencyResponseDto toResponseOrderDto(OrderEfficiency orderEfficiency);
    List<OrderEfficiencyResponseDto> toResponseOrderDtoList(List<OrderEfficiency> orderEfficiency);

    EmployeePerformanceResponseDto toResponseEmployeeDto(EmployeePerformance employeePerformanceList);
    List<EmployeePerformanceResponseDto> toResponseEmployeeDtoList(List<EmployeePerformance> employeePerformanceList);
}
