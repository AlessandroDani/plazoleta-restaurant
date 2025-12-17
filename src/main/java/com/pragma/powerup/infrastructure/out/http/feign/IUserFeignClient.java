package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.out.http.config.FeignClientConfiguration;
import com.pragma.powerup.infrastructure.out.http.request.SmsRequestDto;
import com.pragma.powerup.infrastructure.out.http.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8082", configuration = FeignClientConfiguration.class)
public interface IUserFeignClient {

    @GetMapping("/api/usuarios/check-role/{userId}/{roleName}")
    void checkRole(@PathVariable("userId") Long userId, @PathVariable("roleName") String roleName);

    @PostMapping("/api/sms/send")
    void sendSms(@RequestBody SmsRequestDto request);

    @GetMapping("/api/usuarios/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);


}