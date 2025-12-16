package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.model.EmployeePerformance;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IUserGatewayPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.RoleNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
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
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserHttpAdapter implements IUserGatewayPort {

    private final IUserFeignClient userFeignClient;
    private final ISmsFeignClient smsFeignClient;
    private final IUserRequestMapper userRequestMapper;
    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final ITraceabilityRequestMapper traceabilityRequestMapper;
    private final ITraceabilityFeignResponseMapper traceabilityResponseMapper;

    @Override
    public void isUserOwner(Long userId) {
        checkRole(userId, "PROPIETARIO");
    }

    @Override
    public void isUserEmployee(Long userId) {
        checkRole(userId, "EMPLEADO");
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        SmsRequestDto request = new SmsRequestDto();
        request.setPhoneNumber(phoneNumber);
        request.setMessage(message);

        try {
            smsFeignClient.sendSms(request);
        } catch (FeignException.NotFound e) {
            throw new InvalidRoleException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }

    @Override
    public void saveOrderTrace(Traceability traceability) {
        TraceabilityRequestDto traceabilityRequestDto =
                traceabilityRequestMapper.toRequestDto(traceability);
        try{
            traceabilityFeignClient.saveOrderTrace(traceabilityRequestDto);
        } catch (FeignException.NotFound e) {
            throw new InvalidRoleException();
        }

    }

    @Override
    public User getUserById(Long clientId) {
        try {
            UserResponseDto userResponse = userFeignClient.getUserById(clientId);
            return userRequestMapper.toModel(userResponse);
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }

    }

    @Override
    public List<EmployeePerformance> getEmployeePerformance(Long restaurantId) {
        try {
            List<EmployeePerformanceResponseDto> employees = traceabilityFeignClient.getEmployeesRanking(restaurantId);
            return traceabilityResponseMapper.toEmployeeList(employees);
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }

    @Override
    public List<OrderEfficiency> getOrderEfficiency(Long restaurantId) {
        try {
            List<OrderEfficiencyResponseDto> orders = traceabilityFeignClient.getOrdersEfficiency(restaurantId);
            return traceabilityResponseMapper.toOrderList(orders);
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }

    @Override
    public List<Traceability> getTracesByOrderId(Long orderId) {
        try{
            List<TraceabilityResponseDto> trace = traceabilityFeignClient.getOrderTrace(orderId);
            return traceabilityResponseMapper.toModelList(trace);
        } catch (FeignException.NotFound e) {
            throw new RoleNotFoundException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }

    private void checkRole(Long userId, String role) {
        try {
            userFeignClient.checkRole(userId, role);
        } catch (FeignException.NotFound e) {
            throw new InvalidRoleException();
        } catch (FeignException.FeignServerException e) {
            throw new UserServiceCommunicationException();
        }
    }
}