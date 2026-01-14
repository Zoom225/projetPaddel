package com.padel.config;

import com.padel.model.*;
import com.padel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * DataInitializer - Initialise les données d'exemple pour l'application Padel
 * 
 * Ce composant s'exécute au démarrage de l'application Spring Boot et crée :
 * - 3 sites avec plusieurs terrains chacun
 * - Des membres de différents types (Global, Site, Libre)
 * - Des matches complets avec tous les détails (Date, Terrain, Type, Joueurs, Statut, Paiement)
 * 
 * Emplacement : backend/src/main/java/com/padel/config/DataInitializer.java
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private TerrainRepository terrainRepository;

    @Autowired
    private MembreRepository membreRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Initialiser des données de test si la base est vide
        if (siteRepository.count() == 0) {
            System.out.println("=== Initialisation des données d'exemple ===");
            
            // ============================================================
            // ÉTAPE 1 : CRÉATION DES SITES (3 sites minimum)
            // ============================================================
            System.out.println("Création des sites...");
            
            // Site 1 : Paris Centre - Site principal avec horaires étendus
            Site siteParis = new Site();
            siteParis.setNom("Site Paris Centre");
            siteParis.setHeureDebut(LocalTime.of(8, 0));  // Ouverture à 8h
            siteParis.setHeureFin(LocalTime.of(22, 0));   // Fermeture à 22h
            siteParis.setDureeMatchMinutes(90);          // Matchs de 1h30
            siteParis.setDureeEntreMatchMinutes(15);       // 15 min entre chaque match
            siteParis = siteRepository.save(siteParis);
            System.out.println("✓ Site créé : " + siteParis.getNom());

            // Site 2 : Lyon Nord - Site avec horaires standards
            Site siteLyon = new Site();
            siteLyon.setNom("Site Lyon Nord");
            siteLyon.setHeureDebut(LocalTime.of(9, 0));   // Ouverture à 9h
            siteLyon.setHeureFin(LocalTime.of(21, 0));    // Fermeture à 21h
            siteLyon.setDureeMatchMinutes(90);
            siteLyon.setDureeEntreMatchMinutes(15);
            siteLyon = siteRepository.save(siteLyon);
            System.out.println("✓ Site créé : " + siteLyon.getNom());

            // Site 3 : Marseille Sud - Nouveau site avec horaires flexibles
            Site siteMarseille = new Site();
            siteMarseille.setNom("Site Marseille Sud");
            siteMarseille.setHeureDebut(LocalTime.of(7, 30)); // Ouverture tôt le matin
            siteMarseille.setHeureFin(LocalTime.of(23, 0));   // Fermeture tard le soir
            siteMarseille.setDureeMatchMinutes(90);
            siteMarseille.setDureeEntreMatchMinutes(20);     // Plus de temps entre matchs
            siteMarseille = siteRepository.save(siteMarseille);
            System.out.println("✓ Site créé : " + siteMarseille.getNom());

            // ============================================================
            // ÉTAPE 2 : CRÉATION DES TERRAINS (plusieurs par site)
            // ============================================================
            System.out.println("Création des terrains...");
            
            // Terrains pour Paris Centre (3 terrains)
            Terrain terrainParis1 = new Terrain();
            terrainParis1.setNom("Terrain 1");
            terrainParis1.setSite(siteParis);
            terrainParis1 = terrainRepository.save(terrainParis1);

            Terrain terrainParis2 = new Terrain();
            terrainParis2.setNom("Terrain 2");
            terrainParis2.setSite(siteParis);
            terrainParis2 = terrainRepository.save(terrainParis2);

            Terrain terrainParis3 = new Terrain();
            terrainParis3.setNom("Terrain 3");
            terrainParis3.setSite(siteParis);
            terrainParis3 = terrainRepository.save(terrainParis3);

            // Terrains pour Lyon Nord (2 terrains)
            Terrain terrainLyon1 = new Terrain();
            terrainLyon1.setNom("Terrain 1");
            terrainLyon1.setSite(siteLyon);
            terrainLyon1 = terrainRepository.save(terrainLyon1);

            Terrain terrainLyon2 = new Terrain();
            terrainLyon2.setNom("Terrain 2");
            terrainLyon2.setSite(siteLyon);
            terrainLyon2 = terrainRepository.save(terrainLyon2);

            // Terrains pour Marseille Sud (2 terrains)
            Terrain terrainMarseille1 = new Terrain();
            terrainMarseille1.setNom("Terrain 1");
            terrainMarseille1.setSite(siteMarseille);
            terrainMarseille1 = terrainRepository.save(terrainMarseille1);

            Terrain terrainMarseille2 = new Terrain();
            terrainMarseille2.setNom("Terrain 2");
            terrainMarseille2.setSite(siteMarseille);
            terrainMarseille2 = terrainRepository.save(terrainMarseille2);

            System.out.println("✓ 7 terrains créés au total");

            // ============================================================
            // ÉTAPE 3 : CRÉATION DES MEMBRES (pour les matches)
            // ============================================================
            System.out.println("Création des membres...");
            
            // Membres GLOBAL (peuvent réserver sur tous les sites)
            Membre membreGlobal1 = new Membre();
            membreGlobal1.setMatricule("G0001");
            membreGlobal1.setNom("Martin");
            membreGlobal1.setPrenom("Pierre");
            membreGlobal1.setEmail("pierre.martin@example.com");
            membreGlobal1.setTypeMembre(TypeMembre.GLOBAL);
            membreGlobal1.setSoldeDu(0.0);
            membreGlobal1 = membreRepository.save(membreGlobal1);

            Membre membreGlobal2 = new Membre();
            membreGlobal2.setMatricule("G0002");
            membreGlobal2.setNom("Dupont");
            membreGlobal2.setPrenom("Sophie");
            membreGlobal2.setEmail("sophie.dupont@example.com");
            membreGlobal2.setTypeMembre(TypeMembre.GLOBAL);
            membreGlobal2.setSoldeDu(0.0);
            membreGlobal2 = membreRepository.save(membreGlobal2);

            // Membres SITE (liés à un site spécifique)
            Membre membreSite1 = new Membre();
            membreSite1.setMatricule("S00001");
            membreSite1.setNom("Dubois");
            membreSite1.setPrenom("Marie");
            membreSite1.setEmail("marie.dubois@example.com");
            membreSite1.setTypeMembre(TypeMembre.SITE);
            membreSite1.setSite(siteParis);
            membreSite1.setSoldeDu(0.0);
            membreSite1 = membreRepository.save(membreSite1);

            Membre membreSite2 = new Membre();
            membreSite2.setMatricule("S00002");
            membreSite2.setNom("Lefebvre");
            membreSite2.setPrenom("Thomas");
            membreSite2.setEmail("thomas.lefebvre@example.com");
            membreSite2.setTypeMembre(TypeMembre.SITE);
            membreSite2.setSite(siteLyon);
            membreSite2.setSoldeDu(0.0);
            membreSite2 = membreRepository.save(membreSite2);

            // Membres LIBRE (accès libre, pas de site attitré)
            Membre membreLibre1 = new Membre();
            membreLibre1.setMatricule("L00001");
            membreLibre1.setNom("Bernard");
            membreLibre1.setPrenom("Jean");
            membreLibre1.setEmail("jean.bernard@example.com");
            membreLibre1.setTypeMembre(TypeMembre.LIBRE);
            membreLibre1.setSoldeDu(0.0);
            membreLibre1 = membreRepository.save(membreLibre1);

            Membre membreLibre2 = new Membre();
            membreLibre2.setMatricule("L00002");
            membreLibre2.setNom("Moreau");
            membreLibre2.setPrenom("Claire");
            membreLibre2.setEmail("claire.moreau@example.com");
            membreLibre2.setTypeMembre(TypeMembre.LIBRE);
            membreLibre2.setSoldeDu(0.0);
            membreLibre2 = membreRepository.save(membreLibre2);

            Membre membreLibre3 = new Membre();
            membreLibre3.setMatricule("L00003");
            membreLibre3.setNom("Petit");
            membreLibre3.setPrenom("Lucas");
            membreLibre3.setEmail("lucas.petit@example.com");
            membreLibre3.setTypeMembre(TypeMembre.LIBRE);
            membreLibre3.setSoldeDu(0.0);
            membreLibre3 = membreRepository.save(membreLibre3);

            System.out.println("✓ 7 membres créés (2 Global, 2 Site, 3 Libre)");

            // ============================================================
            // ÉTAPE 3bis : CRÉATION DES ADMINISTRATEURS (auth + rôles)
            // ============================================================
            System.out.println("Création des administrateurs...");

            Administrateur adminGlobal = new Administrateur();
            adminGlobal.setMatricule("AG0001");
            adminGlobal.setNom("Admin");
            adminGlobal.setPrenom("Global");
            adminGlobal.setEmail("admin.global@padel.local");
            adminGlobal.setTypeAdministrateur(TypeAdministrateur.GLOBAL);
            adminGlobal.setPasswordHash(passwordEncoder.encode("admin123"));
            administrateurRepository.save(adminGlobal);

            Administrateur adminSiteParis = new Administrateur();
            adminSiteParis.setMatricule("AS0001");
            adminSiteParis.setNom("Admin");
            adminSiteParis.setPrenom("SiteParis");
            adminSiteParis.setEmail("admin.paris@padel.local");
            adminSiteParis.setTypeAdministrateur(TypeAdministrateur.SITE);
            adminSiteParis.setSite(siteParis);
            adminSiteParis.setPasswordHash(passwordEncoder.encode("admin123"));
            administrateurRepository.save(adminSiteParis);

            System.out.println("✓ 2 admins créés (AG0001 global, AS0001 site Paris) / password: admin123");

            // ============================================================
            // ÉTAPE 4 : CRÉATION DES MATCHES AVEC TOUS LES DÉTAILS
            // ============================================================
            System.out.println("Création des matches d'exemple...");
            
            LocalDate aujourdhui = LocalDate.now();
            LocalDateTime maintenant = LocalDateTime.now();

            // ============================================================
            // MATCH 1 : Match PRIVÉ COMPLET avec PAIEMENTS
            // Date: Aujourd'hui, Terrain: Paris 1, Type: PRIVE, Statut: Complet et Payé
            // ============================================================
            Match match1 = new Match();
            match1.setTerrain(terrainParis1);
            match1.setDate(aujourdhui);
            match1.setHeureDebut(LocalTime.of(10, 0));  // 10h00
            match1.setHeureFin(LocalTime.of(11, 30));   // 11h30
            match1.setTypeMatch(TypeMatch.PRIVE);
            match1.setOrganisateur(membreGlobal1);
            match1.setJoueurs(Arrays.asList(membreGlobal1, membreGlobal2, membreSite1, membreLibre1));
            match1.setPrixTotal(60.0);
            match1.setPrixParJoueur(15.0);
            match1.setEstComplet(true);   // 4 joueurs = complet
            match1.setEstPaye(true);      // Tous les paiements effectués
            match1.setDateCreation(maintenant.minusDays(2));
            match1 = matchRepository.save(match1);

            // Paiements pour le match 1 (tous les joueurs ont payé)
            Paiement paiement1_1 = new Paiement();
            paiement1_1.setMatch(match1);
            paiement1_1.setMembre(membreGlobal1);
            paiement1_1.setMontant(15.0);
            paiement1_1.setDatePaiement(maintenant.minusDays(2));
            paiement1_1.setEstValide(true);
            paiementRepository.save(paiement1_1);

            Paiement paiement1_2 = new Paiement();
            paiement1_2.setMatch(match1);
            paiement1_2.setMembre(membreGlobal2);
            paiement1_2.setMontant(15.0);
            paiement1_2.setDatePaiement(maintenant.minusDays(1));
            paiement1_2.setEstValide(true);
            paiementRepository.save(paiement1_2);

            Paiement paiement1_3 = new Paiement();
            paiement1_3.setMatch(match1);
            paiement1_3.setMembre(membreSite1);
            paiement1_3.setMontant(15.0);
            paiement1_3.setDatePaiement(maintenant.minusDays(1));
            paiement1_3.setEstValide(true);
            paiementRepository.save(paiement1_3);

            Paiement paiement1_4 = new Paiement();
            paiement1_4.setMatch(match1);
            paiement1_4.setMembre(membreLibre1);
            paiement1_4.setMontant(15.0);
            paiement1_4.setDatePaiement(maintenant.minusHours(12));
            paiement1_4.setEstValide(true);
            paiementRepository.save(paiement1_4);

            System.out.println("✓ Match 1 créé : PRIVE, Complet, Payé (4 joueurs, 4 paiements)");

            // ============================================================
            // MATCH 2 : Match PUBLIC INCOMPLET avec PAIEMENTS PARTIELS
            // Date: Aujourd'hui, Terrain: Paris 2, Type: PUBLIC, Statut: Incomplet, Paiements partiels
            // ============================================================
            Match match2 = new Match();
            match2.setTerrain(terrainParis2);
            match2.setDate(aujourdhui);
            match2.setHeureDebut(LocalTime.of(14, 0));  // 14h00
            match2.setHeureFin(LocalTime.of(15, 30));   // 15h30
            match2.setTypeMatch(TypeMatch.PUBLIC);
            match2.setOrganisateur(membreSite1);
            match2.setJoueurs(Arrays.asList(membreSite1, membreLibre2)); // Seulement 2 joueurs sur 4
            match2.setPrixTotal(60.0);
            match2.setPrixParJoueur(15.0);
            match2.setEstComplet(false);  // Pas complet (2/4 joueurs)
            match2.setEstPaye(false);     // Pas tous payés
            match2.setDateCreation(maintenant.minusDays(1));
            match2 = matchRepository.save(match2);

            // Paiements partiels pour le match 2 (seulement 2 joueurs ont payé)
            Paiement paiement2_1 = new Paiement();
            paiement2_1.setMatch(match2);
            paiement2_1.setMembre(membreSite1);
            paiement2_1.setMontant(15.0);
            paiement2_1.setDatePaiement(maintenant.minusHours(6));
            paiement2_1.setEstValide(true);
            paiementRepository.save(paiement2_1);

            Paiement paiement2_2 = new Paiement();
            paiement2_2.setMatch(match2);
            paiement2_2.setMembre(membreLibre2);
            paiement2_2.setMontant(15.0);
            paiement2_2.setDatePaiement(maintenant.minusHours(3));
            paiement2_2.setEstValide(true);
            paiementRepository.save(paiement2_2);

            System.out.println("✓ Match 2 créé : PUBLIC, Incomplet, Paiements partiels (2/4 joueurs)");

            // ============================================================
            // MATCH 3 : Match PRIVÉ COMPLET SANS PAIEMENT
            // Date: Demain, Terrain: Lyon 1, Type: PRIVE, Statut: Complet mais Non payé
            // ============================================================
            Match match3 = new Match();
            match3.setTerrain(terrainLyon1);
            match3.setDate(aujourdhui.plusDays(1));  // Demain
            match3.setHeureDebut(LocalTime.of(16, 0));  // 16h00
            match3.setHeureFin(LocalTime.of(17, 30));   // 17h30
            match3.setTypeMatch(TypeMatch.PRIVE);
            match3.setOrganisateur(membreGlobal2);
            match3.setJoueurs(Arrays.asList(membreGlobal2, membreSite2, membreLibre1, membreLibre3));
            match3.setPrixTotal(60.0);
            match3.setPrixParJoueur(15.0);
            match3.setEstComplet(true);   // 4 joueurs = complet
            match3.setEstPaye(false);     // Aucun paiement effectué
            match3.setDateCreation(maintenant.minusDays(3));
            match3 = matchRepository.save(match3);

            System.out.println("✓ Match 3 créé : PRIVE, Complet, Non payé (4 joueurs, 0 paiement)");

            // ============================================================
            // MATCH 4 : Match PUBLIC COMPLET avec PAIEMENTS
            // Date: Demain, Terrain: Marseille 1, Type: PUBLIC, Statut: Complet et Payé
            // ============================================================
            Match match4 = new Match();
            match4.setTerrain(terrainMarseille1);
            match4.setDate(aujourdhui.plusDays(1));  // Demain
            match4.setHeureDebut(LocalTime.of(18, 0));  // 18h00
            match4.setHeureFin(LocalTime.of(19, 30));   // 19h30
            match4.setTypeMatch(TypeMatch.PUBLIC);
            match4.setOrganisateur(membreLibre3);
            match4.setJoueurs(Arrays.asList(membreLibre3, membreGlobal1, membreSite1, membreLibre2));
            match4.setPrixTotal(60.0);
            match4.setPrixParJoueur(15.0);
            match4.setEstComplet(true);
            match4.setEstPaye(true);
            match4.setDateCreation(maintenant.minusDays(1));
            match4 = matchRepository.save(match4);

            // Paiements pour le match 4
            Paiement paiement4_1 = new Paiement();
            paiement4_1.setMatch(match4);
            paiement4_1.setMembre(membreLibre3);
            paiement4_1.setMontant(15.0);
            paiement4_1.setDatePaiement(maintenant.minusHours(18));
            paiement4_1.setEstValide(true);
            paiementRepository.save(paiement4_1);

            Paiement paiement4_2 = new Paiement();
            paiement4_2.setMatch(match4);
            paiement4_2.setMembre(membreGlobal1);
            paiement4_2.setMontant(15.0);
            paiement4_2.setDatePaiement(maintenant.minusHours(15));
            paiement4_2.setEstValide(true);
            paiementRepository.save(paiement4_2);

            Paiement paiement4_3 = new Paiement();
            paiement4_3.setMatch(match4);
            paiement4_3.setMembre(membreSite1);
            paiement4_3.setMontant(15.0);
            paiement4_3.setDatePaiement(maintenant.minusHours(12));
            paiement4_3.setEstValide(true);
            paiementRepository.save(paiement4_3);

            Paiement paiement4_4 = new Paiement();
            paiement4_4.setMatch(match4);
            paiement4_4.setMembre(membreLibre2);
            paiement4_4.setMontant(15.0);
            paiement4_4.setDatePaiement(maintenant.minusHours(8));
            paiement4_4.setEstValide(true);
            paiementRepository.save(paiement4_4);

            System.out.println("✓ Match 4 créé : PUBLIC, Complet, Payé (4 joueurs, 4 paiements)");

            // ============================================================
            // MATCH 5 : Match PRIVÉ INCOMPLET (en attente de joueurs)
            // Date: Après-demain, Terrain: Paris 3, Type: PRIVE, Statut: Incomplet
            // ============================================================
            Match match5 = new Match();
            match5.setTerrain(terrainParis3);
            match5.setDate(aujourdhui.plusDays(2));  // Après-demain
            match5.setHeureDebut(LocalTime.of(12, 0));  // 12h00
            match5.setHeureFin(LocalTime.of(13, 30));   // 13h30
            match5.setTypeMatch(TypeMatch.PRIVE);
            match5.setOrganisateur(membreGlobal1);
            match5.setJoueurs(Arrays.asList(membreGlobal1, membreLibre1)); // Seulement 2 joueurs
            match5.setPrixTotal(60.0);
            match5.setPrixParJoueur(15.0);
            match5.setEstComplet(false);  // Pas complet (2/4 joueurs)
            match5.setEstPaye(false);      // Pas de paiements
            match5.setDateCreation(maintenant.minusHours(2));
            match5 = matchRepository.save(match5);

            System.out.println("✓ Match 5 créé : PRIVE, Incomplet, Non payé (2/4 joueurs)");

            // ============================================================
            // MATCH 6 : Match PUBLIC avec un seul joueur (ouvert à tous)
            // Date: Après-demain, Terrain: Lyon 2, Type: PUBLIC, Statut: Très incomplet
            // ============================================================
            Match match6 = new Match();
            match6.setTerrain(terrainLyon2);
            match6.setDate(aujourdhui.plusDays(2));  // Après-demain
            match6.setHeureDebut(LocalTime.of(20, 0));  // 20h00
            match6.setHeureFin(LocalTime.of(21, 30));   // 21h30
            match6.setTypeMatch(TypeMatch.PUBLIC);
            match6.setOrganisateur(membreSite2);
            match6.setJoueurs(Arrays.asList(membreSite2)); // Seulement l'organisateur
            match6.setPrixTotal(60.0);
            match6.setPrixParJoueur(15.0);
            match6.setEstComplet(false);
            match6.setEstPaye(false);
            match6.setDateCreation(maintenant.minusHours(1));
            match6 = matchRepository.save(match6);

            System.out.println("✓ Match 6 créé : PUBLIC, Très incomplet (1/4 joueurs)");

            System.out.println("=== Initialisation terminée avec succès ===");
            System.out.println("Résumé :");
            System.out.println("  - 3 sites créés");
            System.out.println("  - 7 terrains créés");
            System.out.println("  - 7 membres créés");
            System.out.println("  - 6 matches créés avec différents statuts");
            System.out.println("  - Paiements associés aux matches");
        }
    }
}

