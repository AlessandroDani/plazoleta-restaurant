package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.out.http.request.SmsRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sms-service", url = "http://localhost:8083")
public interface ISmsFeignClient {
    @PostMapping("/api/sms/send")
    void sendSms(@RequestBody SmsRequestDto request);
}
