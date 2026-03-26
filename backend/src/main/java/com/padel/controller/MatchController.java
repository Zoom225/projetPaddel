package com.padel.controller;

import com.padel.entity.Match;
import com.padel.entity.Paiement;
import com.padel.service.MatchService;
import com.padel.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MatchController {
    private final MatchService matchService;
    private final PaiementService paiementService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/terrain/{terrainId}")
    public ResponseEntity<List<Match>> getMatchesByTerrainId(@PathVariable Long terrainId) {
        return ResponseEntity.ok(matchService.getMatchesByTerrainId(terrainId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Match>> getMatchesByDate(@PathVariable String date) {
        return ResponseEntity.ok(matchService.getMatchesByDate(LocalDate.parse(date)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Match>> getMatchesByType(@PathVariable String type) {
        return ResponseEntity.ok(matchService.getMatchesByType(Match.TypeMatch.valueOf(type)));
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestParam String matriculeOrganisateur,
                                           @RequestParam Long terrainId,
                                           @RequestParam String date,
                                           @RequestParam String heureDebut,
                                           @RequestParam String typeMatch) {
        Match match = matchService.createMatch(matriculeOrganisateur, terrainId, LocalDate.parse(date), LocalTime.parse(heureDebut), Match.TypeMatch.valueOf(typeMatch));
        return ResponseEntity.ok(match);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody Match matchDetails) {
        return ResponseEntity.ok(matchService.updateMatch(id, matchDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matchId}/paiement")
    public ResponseEntity<Paiement> payer(@PathVariable Long matchId, @RequestParam String matricule) {
        Paiement paiement = paiementService.createPaiement(matchId, matricule);
        return ResponseEntity.ok(paiement);
    }
}
