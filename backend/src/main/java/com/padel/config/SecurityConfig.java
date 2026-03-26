package com.padel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/api/auth", "GET")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/sites/**", "POST")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/sites/**", "PUT")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/sites/**", "DELETE")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/terrains/**", "POST")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/terrains/**", "PUT")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/terrains/**", "DELETE")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/membres/**", "POST")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/membres/**", "PUT")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/membres/**", "DELETE")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/statistiques/**", "POST")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/statistiques/**", "PUT")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/statistiques/**", "DELETE")).authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
