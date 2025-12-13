package com.pragma.powerup.infrastructure.out.http.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface IUserFeignClient {

    @GetMapping("/api/usuarios/check-role/{userId}/{roleName}")
    void checkRole(@PathVariable("userId") Long userId, @PathVariable("roleName") String roleName);
}