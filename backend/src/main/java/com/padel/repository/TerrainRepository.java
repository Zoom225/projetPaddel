package com.padel.repository;

import com.padel.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerrainRepository extends JpaRepository<Terrain, Long> {
    @Query("SELECT t FROM Terrain t WHERE t.site.id = :siteId")
    List<Terrain> findBySiteId(@Param("siteId") Long siteId);
}
