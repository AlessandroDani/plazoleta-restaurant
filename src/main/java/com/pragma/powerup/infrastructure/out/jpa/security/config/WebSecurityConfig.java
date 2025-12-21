package com.pragma.powerup.infrastructure.out.jpa.security.config;

import com.pragma.powerup.domain.model.RoleEnum;
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

    private static final String ADMIN = RoleEnum.ADMIN.getDbName();
    private static final String OWNER = RoleEnum.OWNER.getDbName();
    private static final String EMPLOYEE = RoleEnum.EMPLOYEE.getDbName();
    private static final String CLIENT = RoleEnum.CLIENT.getDbName();

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
                .antMatchers(HttpMethod.GET, "/api/restaurantes").hasRole(CLIENT)
                .antMatchers(HttpMethod.POST, "/api/restaurantes").hasRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/api/restaurantes/{id}/empleados").hasRole(OWNER)

                .antMatchers(HttpMethod.GET, "/api/platos").hasRole(CLIENT)
                .antMatchers(HttpMethod.POST, "/api/platos").hasRole(OWNER)
                .antMatchers(HttpMethod.PUT, "/api/platos/{id}").hasRole(OWNER)
                .antMatchers(HttpMethod.PATCH, "/api/platos/{id}").hasRole(OWNER)

                .antMatchers(HttpMethod.GET, "/api/pedidos").hasRole(EMPLOYEE)
                .antMatchers(HttpMethod.GET, "/api/pedidos/{id}/trazabilidad").hasRole(CLIENT)
                .antMatchers(HttpMethod.GET, "/api/pedidos/metricas/tiempos-promedio/{restaurantId}").hasRole(OWNER)
                .antMatchers(HttpMethod.GET, "/api/pedidos/metricas/ranking-empleados/{restaurantId}").hasRole(OWNER)
                .antMatchers(HttpMethod.POST, "/api/pedidos").hasRole(CLIENT)
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/en-preparacion").hasRole(EMPLOYEE)
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/listo").hasRole(EMPLOYEE)
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/entregado").hasRole(EMPLOYEE)
                .antMatchers(HttpMethod.PUT, "/api/pedidos/{id}/cancelado").hasRole(CLIENT)
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
