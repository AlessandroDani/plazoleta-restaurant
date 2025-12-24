package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.model.EmployeePerformance;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IExternalServicesPort;
import com.pragma.powerup.infrastructure.exception.*;
import com.pragma.powerup.infrastructure.out.http.feign.ISmsFeignClient;
import com.pragma.powerup.infrastructure.out.http.feign.ITraceabilityFeignClient;
import com.pragma.powerup.infrastructure.out.http.feign.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.http.mapper.ITraceabilityFeignResponseMapper;
import com.pragma.powerup.infrastructure.out.http.mapper.ITraceabilityRequestMapper;
import com.pragma.powerup.infrastructure.out.http.mapper.IUserRequestMapper;
import com.pragma.powerup.infrastructure.out.http.request.SmsRequestDto;
import com.pragma.powerup.infrastructure.out.http.request.TraceabilityRequestDto;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.UserResponseDto;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ExternalServiceAdapter implements IExternalServicesPort {

    private final IUserFeignClient userFeignClient;
    private final ISmsFeignClient smsFeignClient;
    private final IUserRequestMapper userRequestMapper;
    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final ITraceabilityRequestMapper traceabilityRequestMapper;
    private final ITraceabilityFeignResponseMapper traceabilityResponseMapper;

    private static final String TRACEABILITY = "trazabilidad";
    private static final String USERS = "usuarios";
    private static final String TWILIO = "mensajeria";

    @Override
    public Boolean isUserOwner(Long userId) {
        try {
            return executeExternalCall(() -> {
                ResponseEntity<Boolean> response = userFeignClient.checkRole(userId, "PROPIETARIO");
                return response.getBody();
            }, USERS);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El id del usuario no fue encontrado");
        }
    }

    @Override
    public Boolean isUserEmployee(Long userId) {
        try {
            Boolean hasRole = executeExternalCall(() -> {
                ResponseEntity<Boolean> response = userFeignClient.checkRole(userId, "EMPLEADO");
                return response.getBody();
            }, USERS);
            return hasRole != null && hasRole;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El id del usuario no fue encontrado");
        }
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        SmsRequestDto request = new SmsRequestDto();
        request.setPhoneNumber(phoneNumber);
        request.setMessage(message);
        executeExternalCall(() -> {
            smsFeignClient.sendSms(request);
            return null;
        }, TWILIO);
    }

    @Override
    public void saveOrderTrace(Traceability traceability) {
        TraceabilityRequestDto traceabilityRequestDto = traceabilityRequestMapper.toRequestDto(traceability);
        executeExternalCall(() -> {
            traceabilityFeignClient.saveOrderTrace(traceabilityRequestDto);
            return null;
        }, TRACEABILITY);
    }

    @Override
    public User getUserById(Long clientId) {
        try {
            UserResponseDto userResponse = executeExternalCall(() ->
                    userFeignClient.getUserById(clientId), USERS);
            return userRequestMapper.toModel(userResponse);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El usuario con ID " + clientId + " no fue encontrado.");
        }
    }

    @Override
    public List<EmployeePerformance> getEmployeePerformance(Long restaurantId) {
        List<EmployeePerformanceResponseDto> employees = executeExternalCall(() ->
                traceabilityFeignClient.getEmployeesRanking(restaurantId), TRACEABILITY);
        return traceabilityResponseMapper.toEmployeeList(employees);
    }

    @Override
    public List<OrderEfficiency> getOrderEfficiency(Long restaurantId) {
        List<OrderEfficiencyResponseDto> orders = executeExternalCall(() ->
                traceabilityFeignClient.getOrdersEfficiency(restaurantId), TRACEABILITY);
        return traceabilityResponseMapper.toOrderList(orders);
    }

    @Override
    public List<Traceability> getTracesByOrderId(Long orderId) {
        List<TraceabilityResponseDto> trace = executeExternalCall(() ->
                traceabilityFeignClient.getOrderTrace(orderId), TRACEABILITY);
        return traceabilityResponseMapper.toModelList(trace);
    }

    private <T> T executeExternalCall(Supplier<T> feignCall, String serviceName) {
        try {
            return feignCall.get();
        } catch (ResourceNotFoundException | ActionForbiddenException | ExternalServiceFailureException e) {
            throw e;
        } catch(ExternalServiceUnavailableException  | RetryableException e) {
            throw new ExternalServiceUnavailableException("El servicio de " + serviceName +  " no está disponible o no pudo procesar la solicitud.");
        } catch (Exception e) {
            throw new ExternalServiceUnavailableException("Fallo de conexión o error inesperado con el servicio de " + serviceName);
        }
    }
}