package com.padel.repository;

import com.padel.model.Membre;
import com.padel.model.TypeMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {
    Optional<Membre> findByMatricule(String matricule);
    List<Membre> findByTypeMembre(TypeMembre typeMembre);
    List<Membre> findBySiteId(Long siteId);
}

