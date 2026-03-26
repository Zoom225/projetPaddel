package com.padel.repository;

import com.padel.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTerrainId(Long terrainId);
    List<Match> findByDate(LocalDate date);
    List<Match> findByTypeMatch(Match.TypeMatch typeMatch);
    List<Match> findByOrganisateurId(Long organisateurId);
}

