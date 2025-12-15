package com.pragma.powerup.infrastructure.out.jpa.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final String ADMIN = "ADMINISTRADOR";
    private static final String OWNER = "PROPIETARIO";
    private static final String EMPLOYEE = "EMPLEADO";
    private static final String CLIENT = "CLIENTE";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/restaurantes").hasRole(ADMIN)
                .antMatchers(HttpMethod.GET, "/api/restaurantes").hasRole(CLIENT)
                .antMatchers(HttpMethod.GET, "/api/restaurantes/{id}/platos").hasRole(CLIENT)
                .antMatchers(HttpMethod.POST, "/api/restaurantes/{id}/empleados").hasRole(OWNER)
                .antMatchers(HttpMethod.POST, "/api/platos").hasRole(OWNER)
                .antMatchers(HttpMethod.PUT, "/api/platos/{id}").hasRole(OWNER)
                .antMatchers(HttpMethod.PATCH, "/api/platos/{id}").hasRole(OWNER)
                .antMatchers(HttpMethod.POST, "/api/pedidos").hasRole(CLIENT)
                .antMatchers(HttpMethod.GET, "/api/pedidos").hasRole(EMPLOYEE)

                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/en-preparacion").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/listo").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/entregado").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/cancelado").permitAll()
                .antMatchers(HttpMethod.GET, "/api/pedidos/{id}/trazabilidad").permitAll()

                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
