package com.padel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "🎾 Padel Management Backend - Active");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        response.put("version", "1.0.0");
        response.put("environment", "production");

        Map<String, String> urls = new HashMap<>();
        urls.put("api", "http://localhost:8080/api");
        urls.put("swagger", "http://localhost:8080/swagger-ui/index.html");
        urls.put("openapi", "http://localhost:8080/v3/api-docs");
        urls.put("health", "http://localhost:8080/api/health");
        response.put("urls", urls);

        Map<String, String> services = new HashMap<>();
        services.put("sites", "GET /api/sites - Liste des sites de padel");
        services.put("terrains", "GET /api/terrains - Liste des terrains");
        services.put("membres", "GET /api/membres - Liste des membres");
        services.put("matches", "GET /api/matches - Liste des matchs");
        services.put("administrateurs", "GET /api/administrateurs - Liste des administrateurs");
        services.put("paiements", "GET /api/paiements - Liste des paiements");
        response.put("availableServices", services);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        response.put("application", "Padel Management System");

        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("java_version", System.getProperty("java.version"));
        serverInfo.put("os_name", System.getProperty("os.name"));
        serverInfo.put("available_processors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        response.put("server", serverInfo);

        Map<String, String> database = new HashMap<>();
        database.put("status", "connected");
        database.put("type", "PostgreSQL");
        response.put("database", database);

        return ResponseEntity.ok(response);
    }
}

