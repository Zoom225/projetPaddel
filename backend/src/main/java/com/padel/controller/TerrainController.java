package com.padel.controller;

import com.padel.entity.Terrain;
import com.padel.service.TerrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/terrains")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TerrainController {
    private final TerrainService terrainService;

    @GetMapping
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainService.getAllTerrains());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Long id) {
        return terrainService.getTerrainById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Terrain>> getTerrainsBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(terrainService.getTerrainsBySiteId(siteId));
    }

    @PostMapping
    public ResponseEntity<Terrain> createTerrain(@RequestBody Map<String, Object> payload) {
        String nom = (String) payload.get("nom");
        Integer siteIdInt = (Integer) ((Map<String, Object>) payload.get("site")).get("id");
        Long siteId = siteIdInt.longValue();
        return ResponseEntity.ok(terrainService.createTerrain(nom, siteId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain terrainDetails) {
        return ResponseEntity.ok(terrainService.updateTerrain(id, terrainDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable Long id) {
        terrainService.deleteTerrain(id);
        return ResponseEntity.noContent().build();
    }
}
