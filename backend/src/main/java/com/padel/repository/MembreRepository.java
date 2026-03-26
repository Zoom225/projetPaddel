package com.padel.repository;

import com.padel.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MembreRepository extends JpaRepository<Membre, Long> {
    Optional<Membre> findByMatricule(String matricule);
    Optional<Membre> findByEmail(String email);
    List<Membre> findBySiteId(Long siteId);
    List<Membre> findByTypeMembre(Membre.TypeMembre typeMembre);
}

