package com.padel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> home() {
        return ResponseEntity.ok(Map.of(
            "status", "ok",
            "message", "Padel backend actif",
            "docs", "/swagger-ui/index.html",
            "h2", "/h2-console"
        ));
    }
}
