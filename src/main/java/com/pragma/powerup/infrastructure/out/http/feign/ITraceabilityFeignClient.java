package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.out.http.request.TraceabilityRequestDto;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "traceability-service", url = "http://localhost:8084")
public interface ITraceabilityFeignClient {

    @PostMapping("api/trazabilidad")
    void saveOrderTrace(@RequestBody TraceabilityRequestDto trace);

    @GetMapping("api/trazabilidad/{id}")
    List<TraceabilityResponseDto> getOrderTrace(@PathVariable Long id);

    @GetMapping("api/trazabilidad/eficiencia/pedidos/{restaurantId}")
    List<OrderEfficiencyResponseDto> getOrdersEfficiency(@PathVariable Long restaurantId);

    @GetMapping("api/trazabilidad/empleados/ranking/{restaurantId}")
    List<EmployeePerformanceResponseDto> getEmployeesRanking(@PathVariable Long restaurantId);
}
