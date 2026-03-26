package com.padel.controller;

import com.padel.entity.Paiement;
import com.padel.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaiementController {
    private final PaiementService paiementService;

    @GetMapping
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        return ResponseEntity.ok(paiementService.getAllPaiements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<Paiement>> getPaiementsByMatchId(@PathVariable Long matchId) {
        return ResponseEntity.ok(paiementService.getPaiementsByMatchId(matchId));
    }

    @GetMapping("/membre/{membreId}")
    public ResponseEntity<List<Paiement>> getPaiementsByMembreId(@PathVariable Long membreId) {
        return ResponseEntity.ok(paiementService.getPaiementsByMembreId(membreId));
    }

    @PostMapping
    public ResponseEntity<Paiement> createPaiement(
            @RequestParam Long matchId,
            @RequestParam String matricule) {
        return ResponseEntity.ok(paiementService.createPaiement(matchId, matricule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paiement> updatePaiement(@PathVariable Long id, @RequestBody Paiement paiementDetails) {
        return ResponseEntity.ok(paiementService.updatePaiement(id, paiementDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        paiementService.deletePaiement(id);
        return ResponseEntity.noContent().build();
    }
}

