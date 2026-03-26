package com.padel.service;

import com.padel.entity.Site;
import com.padel.entity.Terrain;
import com.padel.repository.SiteRepository;
import com.padel.repository.TerrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TerrainService {
    private final TerrainRepository terrainRepository;
    private final SiteRepository siteRepository;

    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    public Optional<Terrain> getTerrainById(Long id) {
        return terrainRepository.findById(id);
    }

    public List<Terrain> getTerrainsBySiteId(Long siteId) {
        return terrainRepository.findBySiteId(siteId);
    }

    public Terrain createTerrain(String nom, Long siteId) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));
        Terrain terrain = new Terrain();
        terrain.setNom(nom);
        terrain.setSite(site);
        return terrainRepository.save(terrain);
    }

    public Terrain updateTerrain(Long id, Terrain terrainDetails) {
        return terrainRepository.findById(id).map(terrain -> {
            terrain.setNom(terrainDetails.getNom());
            terrain.setSite(terrainDetails.getSite());
            return terrainRepository.save(terrain);
        }).orElseThrow(() -> new RuntimeException("Terrain not found"));
    }

    public void deleteTerrain(Long id) {
        terrainRepository.deleteById(id);
    }
}
