package com.padel.service;

import com.padel.entity.Match;
import com.padel.entity.Membre;
import com.padel.entity.Terrain;
import com.padel.repository.MatchRepository;
import com.padel.repository.MembreRepository;
import com.padel.repository.TerrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MembreRepository membreRepository;
    private final TerrainRepository terrainRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public List<Match> getMatchesByTerrainId(Long terrainId) {
        return matchRepository.findByTerrainId(terrainId);
    }

    public List<Match> getMatchesByDate(LocalDate date) {
        return matchRepository.findByDate(date);
    }

    public List<Match> getMatchesByType(Match.TypeMatch typeMatch) {
        return matchRepository.findByTypeMatch(typeMatch);
    }

    public Match createMatch(String matriculeOrganisateur, Long terrainId, LocalDate date, LocalTime heureDebut, Match.TypeMatch typeMatch) {
        Membre organisateur = membreRepository.findByMatricule(matriculeOrganisateur)
                .orElseThrow(() -> new RuntimeException("Membre not found"));
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new RuntimeException("Terrain not found"));

        LocalTime heureFin = heureDebut.plusMinutes(terrain.getSite().getDureeMatchMinutes());

        Match match = Match.builder()
                .organisateur(organisateur)
                .terrain(terrain)
                .date(date)
                .heureDebut(heureDebut)
                .heureFin(heureFin)
                .typeMatch(typeMatch)
                .build();

        return matchRepository.save(match);
    }

    public Match updateMatch(Long id, Match matchDetails) {
        return matchRepository.findById(id).map(match -> {
            match.setDate(matchDetails.getDate());
            match.setHeureDebut(matchDetails.getHeureDebut());
            match.setHeureFin(matchDetails.getHeureFin());
            match.setTypeMatch(matchDetails.getTypeMatch());
            match.setPrixTotal(matchDetails.getPrixTotal());
            match.setPrixParJoueur(matchDetails.getPrixParJoueur());
            match.setEstComplet(matchDetails.getEstComplet());
            match.setEstPaye(matchDetails.getEstPaye());
            return matchRepository.save(match);
        }).orElseThrow(() -> new RuntimeException("Match not found"));
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
