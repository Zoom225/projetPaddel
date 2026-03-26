package com.padel.repository;

import com.padel.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByMatchId(Long matchId);
    List<Paiement> findByMembreId(Long membreId);
}

