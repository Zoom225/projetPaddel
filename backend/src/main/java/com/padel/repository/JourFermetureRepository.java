package com.padel.repository;

import com.padel.model.JourFermeture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JourFermetureRepository extends JpaRepository<JourFermeture, Long> {
    List<JourFermeture> findBySiteId(Long siteId);
    List<JourFermeture> findBySiteIsNull(); // Fermetures globales
    List<JourFermeture> findByDate(LocalDate date);
    List<JourFermeture> findByDateAndSiteId(LocalDate date, Long siteId);
}

