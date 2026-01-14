package com.padel.service;

import com.padel.model.Match;
import com.padel.model.Paiement;
import com.padel.repository.MatchRepository;
import com.padel.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatistiqueService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;

    public Double getChiffreAffaires(LocalDateTime debut, LocalDateTime fin) {
        Double ca = paiementRepository.getChiffreAffaires(debut, fin);
        return ca != null ? ca : 0.0;
    }

    public Map<String, Object> getStatistiquesSite(Long siteId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Match> matches = matchRepository.findByTerrainSiteId(siteId);
        
        stats.put("totalMatches", matches.size());
        stats.put("matchesComplets", matches.stream().filter(Match::getEstComplet).count());
        stats.put("matchesPayes", matches.stream().filter(Match::getEstPaye).count());
        
        LocalDateTime debutMois = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime finMois = LocalDateTime.now();
        stats.put("chiffreAffairesMois", getChiffreAffaires(debutMois, finMois));
        
        return stats;
    }

    public Map<String, Object> getStatistiquesGlobales() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Match> matches = matchRepository.findAll();
        
        stats.put("totalMatches", matches.size());
        stats.put("matchesComplets", matches.stream().filter(Match::getEstComplet).count());
        stats.put("matchesPayes", matches.stream().filter(Match::getEstPaye).count());
        
        LocalDateTime debutMois = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime finMois = LocalDateTime.now();
        stats.put("chiffreAffairesMois", getChiffreAffaires(debutMois, finMois));
        
        return stats;
    }
}

