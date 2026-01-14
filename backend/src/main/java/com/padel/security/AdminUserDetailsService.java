package com.padel.security;

import com.padel.model.Administrateur;
import com.padel.repository.AdministrateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Charge les administrateurs depuis la DB.
 * "username" = matricule admin.
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdministrateurRepository administrateurRepository;

    public AdminUserDetailsService(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrateur admin = administrateurRepository.findByMatricule(username)
                .orElseThrow(() -> new UsernameNotFoundException("Administrateur introuvable: " + username));

        String role = switch (admin.getTypeAdministrateur()) {
            case GLOBAL -> "ADMIN_GLOBAL";
            case SITE -> "ADMIN_SITE";
        };

        return User
            .withUsername(admin.getMatricule())
            .password(admin.getPasswordHash())
            .roles(role)
            .build();
    }
}
