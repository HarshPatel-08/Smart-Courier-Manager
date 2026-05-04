package com.Harsh.Smart.Courier.Manager.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private jwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/api/auth/login").permitAll()
                        // Customer only
                        .requestMatchers(HttpMethod.POST, "/order/create").hasRole("CUSTOMER")
                        .requestMatchers("/order/my-orders").hasRole("CUSTOMER")
                        // Admin + Manager only
                        .requestMatchers("/order/all").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/assignments/assign").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/assignments/bulk-assign").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/assignments/all").hasAnyRole("ADMIN", "MANAGER")
                        // Agent only
                        .requestMatchers("/api/assignments/my-assignments").hasRole("AGENT")
                        // Any authenticated user
                        .anyRequest().authenticated()
                )
                .build();
    }
}
