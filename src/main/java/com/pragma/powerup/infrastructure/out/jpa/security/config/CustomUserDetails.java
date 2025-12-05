package com.pragma.powerup.infrastructure.out.jpa.security.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

@Getter
public class CustomUserDetails extends User {

    private final Long id;
    private final String roleName;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String roleName) {
        super(username, password, authorities);
        this.id = id;
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, roleName);
    }
}
