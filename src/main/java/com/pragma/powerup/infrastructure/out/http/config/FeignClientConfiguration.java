package com.pragma.powerup.infrastructure.out.http.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfiguration {
    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (requestAttributes != null) {
                String authorization = requestAttributes.getRequest().getHeader("Authorization");

                if (authorization != null && authorization.startsWith("Bearer ")) {
                    requestTemplate.header("Authorization", authorization);
                }
            }
        };
    }

}
