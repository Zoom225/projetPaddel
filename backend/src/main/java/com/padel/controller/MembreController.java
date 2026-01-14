package com.padel.controller;

import com.padel.model.Membre;
import com.padel.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/membres")
@CrossOrigin(origins = "http://localhost:4200")
public class MembreController {
    
    @Autowired
    private MembreService membreService;

    @GetMapping
    public ResponseEntity<List<Membre>> getAllMembres() {
        return ResponseEntity.ok(membreService.getAllMembres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membre> getMembreById(@PathVariable Long id) {
        Optional<Membre> membre = membreService.getMembreById(id);
        return membre.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Membre> getMembreByMatricule(@PathVariable String matricule) {
        Optional<Membre> membre = membreService.getMembreByMatricule(matricule);
        return membre.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Membre> createMembre(@RequestBody Membre membre) {
        try {
            Membre created = membreService.createMembre(membre);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membre> updateMembre(@PathVariable Long id, @RequestBody Membre membre) {
        try {
            Membre updated = membreService.updateMembre(id, membre);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembre(@PathVariable Long id) {
        try {
            membreService.deleteMembre(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

