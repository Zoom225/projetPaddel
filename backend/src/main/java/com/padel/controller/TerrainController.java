package com.padel.controller;

import com.padel.model.Site;
import com.padel.model.Terrain;
import com.padel.repository.SiteRepository;
import com.padel.repository.TerrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {
    
    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private SiteRepository siteRepository;

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Terrain>> getTerrainsBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(terrainRepository.findBySiteId(siteId));
    }

    @GetMapping
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Terrain> createTerrain(@RequestBody Terrain terrain) {
        if (terrain.getSite() == null || terrain.getSite().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Site> site = siteRepository.findById(terrain.getSite().getId());
        if (site.isEmpty()) return ResponseEntity.badRequest().build();

        terrain.setId(null);
        terrain.setSite(site.get());
        Terrain created = terrainRepository.save(terrain);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain terrain) {
        Optional<Terrain> existingOpt = terrainRepository.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        Terrain existing = existingOpt.get();
        existing.setNom(terrain.getNom());

        // Changement de site optionnel
        if (terrain.getSite() != null && terrain.getSite().getId() != null) {
            Optional<Site> site = siteRepository.findById(terrain.getSite().getId());
            if (site.isEmpty()) return ResponseEntity.badRequest().build();
            existing.setSite(site.get());
        }

        return ResponseEntity.ok(terrainRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable Long id) {
        if (!terrainRepository.existsById(id)) return ResponseEntity.notFound().build();
        terrainRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

