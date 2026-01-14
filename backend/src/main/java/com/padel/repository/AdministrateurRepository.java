package com.padel.repository;

import com.padel.model.Administrateur;
import com.padel.model.TypeAdministrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByMatricule(String matricule);
    List<Administrateur> findByTypeAdministrateur(TypeAdministrateur type);
    List<Administrateur> findBySiteId(Long siteId);
}

