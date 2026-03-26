package com.padel.controller;

import com.padel.entity.Administrateur;
import com.padel.service.AdministrateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/administrateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdministrateurController {
    private final AdministrateurService administrateurService;

    @GetMapping
    public ResponseEntity<List<Administrateur>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.getAllAdministrateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdministrateurById(@PathVariable Long id) {
        return administrateurService.getAdministrateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Administrateur> getAdministrateurByMatricule(@PathVariable String matricule) {
        return administrateurService.getAdministrateurByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Administrateur> getAdministrateurByEmail(@PathVariable String email) {
        return administrateurService.getAdministrateurByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Administrateur>> getAdministrateursBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(administrateurService.getAdministrateursBySiteId(siteId));
    }

    @PostMapping
    public ResponseEntity<Administrateur> createAdministrateur(@RequestBody Administrateur administrateur) {
        return ResponseEntity.ok(administrateurService.createAdministrateur(administrateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrateur> updateAdministrateur(@PathVariable Long id, @RequestBody Administrateur administrateurDetails) {
        return ResponseEntity.ok(administrateurService.updateAdministrateur(id, administrateurDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable Long id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }
}

