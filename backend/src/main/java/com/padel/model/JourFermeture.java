package com.padel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "jours_fermeture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourFermeture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site; // null pour fermeture globale

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String raison;
}

