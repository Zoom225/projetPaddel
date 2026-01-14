package com.padel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Mot de passe hashé (BCrypt).
     * 
     * Remarque : les joueurs (membres) n'ont pas de login dans le cahier des charges.
     * Seuls les administrateurs s'authentifient (exigence PDW : auth + rôles).
     */
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAdministrateur typeAdministrateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site; // null pour GLOBAL, obligatoire pour SITE
}

