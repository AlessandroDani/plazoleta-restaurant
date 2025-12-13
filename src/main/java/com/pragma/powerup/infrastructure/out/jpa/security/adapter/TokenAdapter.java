package com.pragma.powerup.infrastructure.out.jpa.security.adapter;

import com.pragma.powerup.domain.spi.ITokenPort;
import com.pragma.powerup.infrastructure.out.jpa.security.config.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TokenAdapter implements ITokenPort {
    @Override
    public Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        return null;
    }
}
