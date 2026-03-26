package com.padel.listener;

import com.padel.entity.*;
import com.padel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final SiteRepository siteRepository;
    private final TerrainRepository terrainRepository;
    private final AdministrateurRepository administrateurRepository;
    private final MembreRepository membreRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        // Vérifier si les données existent déjà
        if (siteRepository.count() > 0) {
            System.out.println("✓ Base de données déjà initialisée");
            return;
        }

        System.out.println("🚀 Initialisation des données de démarrage...");

        // Créer les sites
        Site siteParis = Site.builder()
                .nom("Site Paris Centre")
                .heureDebut(LocalTime.of(8, 0))
                .heureFin(LocalTime.of(22, 0))
                .dureeMatchMinutes(90)
                .dureeEntreMatchMinutes(15)
                .build();

        Site siteLyon = Site.builder()
                .nom("Site Lyon Nord")
                .heureDebut(LocalTime.of(9, 0))
                .heureFin(LocalTime.of(21, 0))
                .dureeMatchMinutes(90)
                .dureeEntreMatchMinutes(15)
                .build();

        siteParis = siteRepository.save(siteParis);
        siteLyon = siteRepository.save(siteLyon);

        // Créer les terrains
        Terrain terrain1 = Terrain.builder()
                .nom("Terrain 1")
                .site(siteParis)
                .build();

        Terrain terrain2 = Terrain.builder()
                .nom("Terrain 2")
                .site(siteParis)
                .build();

        Terrain terrainA = Terrain.builder()
                .nom("Terrain A")
                .site(siteLyon)
                .build();

        terrainRepository.save(terrain1);
        terrainRepository.save(terrain2);
        terrainRepository.save(terrainA);

        // Créer un administrateur global
        Administrateur adminGlobal = Administrateur.builder()
                .matricule("ADMIN001")
                .nom("Dupont")
                .prenom("Jean")
                .email("admin@padel.com")
                .passwordHash("$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy70l4e") // password: admin123
                .typeAdministrateur(Administrateur.TypeAdministrateur.GLOBAL)
                .site(null)
                .build();

        administrateurRepository.save(adminGlobal);

        // Créer des membres
        Membre membre1 = Membre.builder()
                .matricule("MEM001")
                .nom("Martin")
                .prenom("Pierre")
                .email("pierre.martin@example.com")
                .typeMembre(Membre.TypeMembre.GLOBAL)
                .site(null)
                .soldeDu(0.0)
                .build();

        Membre membre2 = Membre.builder()
                .matricule("MEM002")
                .nom("Bernard")
                .prenom("Marie")
                .email("marie.bernard@example.com")
                .typeMembre(Membre.TypeMembre.SITE)
                .site(siteParis)
                .soldeDu(0.0)
                .build();

        membreRepository.save(membre1);
        membreRepository.save(membre2);

        System.out.println("✓ Données de démarrage créées avec succès!");
        System.out.println("  - 2 Sites créés");
        System.out.println("  - 3 Terrains créés");
        System.out.println("  - 1 Administrateur créé");
        System.out.println("  - 2 Membres créés");
    }
}

