package com.padel.controller;

import com.padel.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {
    
    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping("/globales")
    public ResponseEntity<Map<String, Object>> getStatistiquesGlobales() {
        return ResponseEntity.ok(statistiqueService.getStatistiquesGlobales());
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<Map<String, Object>> getStatistiquesSite(@PathVariable Long siteId) {
        return ResponseEntity.ok(statistiqueService.getStatistiquesSite(siteId));
    }

    @GetMapping("/chiffre-affaires")
    public ResponseEntity<Double> getChiffreAffaires(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(statistiqueService.getChiffreAffaires(debut, fin));
    }
}

