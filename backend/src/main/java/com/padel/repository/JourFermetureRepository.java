package com.padel.repository;

import com.padel.entity.JourFermeture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JourFermetureRepository extends JpaRepository<JourFermeture, Long> {
    List<JourFermeture> findBySiteId(Long siteId);
    List<JourFermeture> findByDate(LocalDate date);
}

