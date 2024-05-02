package com.example.demologin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demologin.filter.JwtAuthenticationFilter;
import com.example.demologin.service.UserDetailsServiceImp;

import io.jsonwebtoken.security.Password;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImp userDetailsServiceImp;

    

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsServiceImp userDetailsServiceImp) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeRequests(
                     req->req.requestMatchers("/login", "/register")
                             .permitAll()
                             .anyRequest()
                             .authenticated()
               ).userDetailsService(userDetailsServiceImp)
               .sessionManagement(
                     session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
