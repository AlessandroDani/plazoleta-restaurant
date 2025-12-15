package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.EmployeePerformance;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.model.User;

public interface IUserGatewayPort {
    void isUserOwner(Long userId);
    void isUserEmployee(Long userId);
    void sendSms(String phoneNumber, String message);
    void saveOrderTrace(Traceability traceability);
    User getUserById(Long clientId);
    EmployeePerformance getEmployeePerformanceById();
    OrderEfficiency getOrderEfficiencyById();

}