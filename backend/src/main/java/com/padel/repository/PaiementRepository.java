package com.padel.repository;

import com.padel.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByMatchId(Long matchId);
    List<Paiement> findByMembreId(Long membreId);
    
    @Query("SELECT p FROM Paiement p WHERE p.match.id = :matchId AND p.membre.id = :membreId")
    Optional<Paiement> findByMatchIdAndMembreId(@Param("matchId") Long matchId, @Param("membreId") Long membreId);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.match.id = :matchId AND p.estValide = true")
    Double getTotalPaiementsByMatchId(@Param("matchId") Long matchId);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.datePaiement BETWEEN :debut AND :fin")
    Double getChiffreAffaires(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
}

