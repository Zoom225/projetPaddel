package com.padel.repository;

import com.padel.model.Match;
import com.padel.model.TypeMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTerrainSiteId(Long siteId);
    List<Match> findByTypeMatch(TypeMatch typeMatch);
    List<Match> findByDate(LocalDate date);
    List<Match> findByDateAndTerrainSiteId(LocalDate date, Long siteId);
    
    @Query("SELECT m FROM Match m WHERE m.organisateur.id = :membreId")
    List<Match> findByOrganisateurId(@Param("membreId") Long membreId);
    
    @Query("SELECT m FROM Match m JOIN m.joueurs j WHERE j.id = :membreId")
    List<Match> findByJoueurId(@Param("membreId") Long membreId);
    
    @Query("SELECT m FROM Match m WHERE m.typeMatch = :type AND m.date >= :date")
    List<Match> findPublicMatchesFromDate(@Param("type") TypeMatch type, @Param("date") LocalDate date);
}

