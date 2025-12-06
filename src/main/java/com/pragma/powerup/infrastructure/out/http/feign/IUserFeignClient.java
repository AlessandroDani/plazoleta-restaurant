package com.pragma.powerup.infrastructure.out.http.feign;

import com.pragma.powerup.infrastructure.out.http.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface IUserFeignClient {

    @GetMapping("/api/usuario/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}