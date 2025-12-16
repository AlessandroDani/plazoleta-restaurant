package com.pragma.powerup.infrastructure.out.http.mapper;

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
public interface ITraceabilityFeignResponseMapper {
    Traceability toModel(TraceabilityResponseDto traceabilityResponseDto);
    List<Traceability> toModelList(List<TraceabilityResponseDto> traceabilityResponseDtoList);

    OrderEfficiency toOrder(OrderEfficiencyResponseDto orderEfficiencyResponseDto);
    List<OrderEfficiency> toOrderList(List<OrderEfficiencyResponseDto> orderEfficiencyResponseDtoList);

    EmployeePerformance toEmployee(EmployeePerformanceResponseDto employeePerformanceResponseDto);
    List<EmployeePerformance> toEmployeeList(List<EmployeePerformanceResponseDto> employeePerformanceResponseDtoList);
}
