package com.padel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Sécurité :
 * - Les MEMBRES n'ont pas de login (cahier des charges SGBD). Ils utilisent leur matricule dans les appels API.
 * - Les ADMINISTRATEURS doivent être authentifiés, avec des rôles distincts (PDW : auth + rôles).
 *
 * Implémentation simple et robuste : HTTP Basic Auth sur les endpoints d'administration.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics (lecture)
                .requestMatchers(HttpMethod.GET, "/api/sites/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/terrains/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/matches/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/membres/**").permitAll()

                // Endpoints d'auth
                .requestMatchers("/api/auth/**").authenticated()

                // Statistiques : réservé aux admins
                .requestMatchers("/api/statistiques/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")

                // Mutations (CRUD) : réservé aux admins
                .requestMatchers(HttpMethod.POST, "/api/sites/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.PUT, "/api/sites/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.DELETE, "/api/sites/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")

                .requestMatchers(HttpMethod.POST, "/api/terrains/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.PUT, "/api/terrains/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.DELETE, "/api/terrains/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")

                .requestMatchers(HttpMethod.POST, "/api/membres/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.PUT, "/api/membres/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")
                .requestMatchers(HttpMethod.DELETE, "/api/membres/**").hasAnyRole("ADMIN_GLOBAL", "ADMIN_SITE")

                // Opérations de réservation/paiement : publiques (règles gérées côté service)
                .requestMatchers(HttpMethod.POST, "/api/matches/**").permitAll()

                // H2 console (dev uniquement)
                .requestMatchers("/h2-console/**").permitAll()

                // Swagger / OpenAPI (dev/test)
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        // Pour l'H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
