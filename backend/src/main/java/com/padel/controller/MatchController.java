package com.padel.controller;

import com.padel.model.Match;
import com.padel.model.Paiement;
import com.padel.model.TypeMatch;
import com.padel.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        Optional<Match> match = matchService.getMatchById(id);
        return match.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<Match>> getMatchesBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(matchService.getMatchesBySite(siteId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<Match>> getPublicMatches() {
        return ResponseEntity.ok(matchService.getPublicMatches());
    }

    @GetMapping("/public/from-date")
    public ResponseEntity<List<Match>> getPublicMatchesFromDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(matchService.getPublicMatchesFromDate(date));
    }

    @GetMapping("/membre/{matricule}")
    public ResponseEntity<List<Match>> getMatchesByMembre(@PathVariable String matricule) {
        try {
            return ResponseEntity.ok(matchService.getMatchesByMembre(matricule));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(
            @RequestParam String matriculeOrganisateur,
            @RequestParam Long terrainId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam TypeMatch typeMatch) {
        try {
            Match match = matchService.createMatch(matriculeOrganisateur, terrainId, date, heureDebut, typeMatch);
            return ResponseEntity.status(HttpStatus.CREATED).body(match);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{matchId}/joueurs")
    public ResponseEntity<Match> ajouterJoueurMatchPrive(
            @PathVariable Long matchId,
            @RequestParam String matriculeJoueur) {
        try {
            Match match = matchService.ajouterJoueurMatchPrive(matchId, matriculeJoueur);
            return ResponseEntity.ok(match);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{matchId}/paiement")
    public ResponseEntity<Paiement> effectuerPaiement(
            @PathVariable Long matchId,
            @RequestParam String matricule) {
        try {
            Paiement paiement = matchService.effectuerPaiement(matchId, matricule);
            return ResponseEntity.status(HttpStatus.CREATED).body(paiement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

