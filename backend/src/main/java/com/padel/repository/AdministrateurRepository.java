package com.padel.repository;

import com.padel.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByMatricule(String matricule);
    Optional<Administrateur> findByEmail(String email);
    List<Administrateur> findBySiteId(Long siteId);
}

