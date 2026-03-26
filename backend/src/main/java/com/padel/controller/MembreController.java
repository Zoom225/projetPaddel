package com.padel.controller;

import com.padel.entity.Membre;
import com.padel.service.MembreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/membres")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MembreController {
    private final MembreService membreService;

    @GetMapping
    public ResponseEntity<List<Membre>> getAllMembres() {
        return ResponseEntity.ok(membreService.getAllMembres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membre> getMembreById(@PathVariable Long id) {
        return membreService.getMembreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Membre> getMembreByMatricule(@PathVariable String matricule) {
        return membreService.getMembreByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Membre> getMembreByEmail(@PathVariable String email) {
        return membreService.getMembreByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Membre>> getMembresBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(membreService.getMembresBySiteId(siteId));
    }

    @PostMapping
    public ResponseEntity<Membre> createMembre(@RequestBody Membre membre) {
        return ResponseEntity.ok(membreService.createMembre(membre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membre> updateMembre(@PathVariable Long id, @RequestBody Membre membreDetails) {
        return ResponseEntity.ok(membreService.updateMembre(id, membreDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembre(@PathVariable Long id) {
        membreService.deleteMembre(id);
        return ResponseEntity.noContent().build();
    }
}

