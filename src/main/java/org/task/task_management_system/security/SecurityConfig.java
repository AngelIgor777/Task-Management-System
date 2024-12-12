package org.task.task_management_system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.task.task_management_system.filter.EmailPasswordAuthenticationFilter;
import org.task.task_management_system.filter.AuthFilter;
import org.task.task_management_system.jwt.service.JwtService;
import org.task.task_management_system.repository.UserRepository;
import org.task.task_management_system.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/login").permitAll() // Доступ для логина
                .antMatchers("/api/v1/auth/**").permitAll() // Другие эндпоинты аутентификации
                .anyRequest().authenticated() // Остальные требуют аутентификации
                .and()
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class); // Проверка JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter(HttpSecurity http) throws Exception {
        return new EmailPasswordAuthenticationFilter(authenticationManager(http));
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(jwtService);
    }
}
