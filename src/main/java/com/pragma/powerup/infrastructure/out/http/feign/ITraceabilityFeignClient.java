package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "traceability-service", url = "http://localhost:8084")
public interface ITraceabilityFeignClient {

    @GetMapping("api/trazabilidad/eficiencia/pedidos")
    OrderEfficiencyResponseDto getOrdersEfficiency();

    @GetMapping("api/trazabilidad/empleados/ranking")
    EmployeePerformanceResponseDto getEmployeesRanking();
}
