package com.padel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeMatch typeMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Membre organisateur;

    @Column(nullable = false)
    private Double prixTotal = 60.0;

    @Column(nullable = false)
    private Double prixParJoueur = 15.0;

    @Column(nullable = false)
    private Boolean estComplet = false;

    @Column(nullable = false)
    private Boolean estPaye = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateConversionPublic;

    @ManyToMany
    @JoinTable(
            name = "match_joueurs",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "membre_id")
    )
    private List<Membre> joueurs;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements;

    public enum TypeMatch {
        PRIVE, PUBLIC
    }
}

