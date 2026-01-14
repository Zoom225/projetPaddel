package com.padel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrain_id", nullable = false)
    private Terrain terrain;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMatch typeMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Membre organisateur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "match_joueurs",
        joinColumns = @JoinColumn(name = "match_id"),
        inverseJoinColumns = @JoinColumn(name = "membre_id")
    )
    private List<Membre> joueurs = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements = new ArrayList<>();

    @Column(nullable = false)
    private Double prixTotal = 60.0;

    @Column(nullable = false)
    private Double prixParJoueur = 15.0; // 60 / 4

    @Column(nullable = false)
    private Boolean estComplet = false;

    @Column(nullable = false)
    private Boolean estPaye = false;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column
    private LocalDateTime dateConversionPublic; // Quand un match privé devient public
}

