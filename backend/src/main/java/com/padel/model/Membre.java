package com.padel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricule; // Gxxxx, Sxxxxx, Lxxxxx

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMembre typeMembre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site; // null pour GLOBAL et LIBRE, obligatoire pour SITE

    @OneToMany(mappedBy = "organisateur", cascade = CascadeType.ALL)
    private List<Match> matchesOrganises = new ArrayList<>();

    @ManyToMany(mappedBy = "joueurs")
    private List<Match> matchesParticipes = new ArrayList<>();

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL)
    private List<Paiement> paiements = new ArrayList<>();

    @Column(nullable = false)
    private Double soldeDu = 0.0; // Solde dû pour les matches non complétés
}

