package com.padel.service;

import com.padel.model.*;
import com.padel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private MembreRepository membreRepository;
    
    @Autowired
    private TerrainRepository terrainRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private JourFermetureRepository jourFermetureRepository;
    
    @Autowired
    private MembreService membreService;

    private static final int DUREE_MATCH_MINUTES = 90;
    private static final int DUREE_ENTRE_MATCH_MINUTES = 15;
    private static final int NOMBRE_JOUEURS_REQUIS = 4;
    private static final double PRIX_MATCH = 60.0;
    private static final double PRIX_PAR_JOUEUR = 15.0;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public List<Match> getMatchesBySite(Long siteId) {
        return matchRepository.findByTerrainSiteId(siteId);
    }

    public List<Match> getPublicMatches() {
        return matchRepository.findByTypeMatch(TypeMatch.PUBLIC);
    }

    public List<Match> getPublicMatchesFromDate(LocalDate date) {
        return matchRepository.findPublicMatchesFromDate(TypeMatch.PUBLIC, date);
    }

    public List<Match> getMatchesByMembre(String matricule) {
        Membre membre = membreRepository.findByMatricule(matricule)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        List<Match> matchesOrganises = matchRepository.findByOrganisateurId(membre.getId());
        List<Match> matchesParticipes = matchRepository.findByJoueurId(membre.getId());
        
        matchesOrganises.addAll(matchesParticipes);
        return matchesOrganises.stream().distinct().collect(Collectors.toList());
    }

    public Match createMatch(String matriculeOrganisateur, Long terrainId, LocalDate date, 
                            LocalTime heureDebut, TypeMatch typeMatch) {
        Membre organisateur = membreRepository.findByMatricule(matriculeOrganisateur)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        Terrain terrain = terrainRepository.findById(terrainId)
            .orElseThrow(() -> new RuntimeException("Terrain non trouvé"));
        
        // Vérifier le délai de réservation selon le type de membre
        validateDelaiReservation(organisateur, date);
        
        // Vérifier si le membre a un solde dû
        if (organisateur.getSoldeDu() > 0) {
            throw new RuntimeException("Impossible de réserver : solde dû de " + organisateur.getSoldeDu() + "€");
        }
        
        // Vérifier si le terrain est disponible
        validateDisponibiliteTerrain(terrain, date, heureDebut);
        
        // Vérifier les jours de fermeture
        validateJourFermeture(terrain.getSite(), date);
        
        Match match = new Match();
        match.setTerrain(terrain);
        match.setDate(date);
        match.setHeureDebut(heureDebut);
        match.setHeureFin(heureDebut.plusMinutes(DUREE_MATCH_MINUTES));
        match.setTypeMatch(typeMatch);
        match.setOrganisateur(organisateur);
        match.setPrixTotal(PRIX_MATCH);
        match.setPrixParJoueur(PRIX_PAR_JOUEUR);
        match.setDateCreation(LocalDateTime.now());
        match.setEstComplet(false);
        match.setEstPaye(false);
        
        // Si match privé, ajouter l'organisateur comme joueur
        if (typeMatch == TypeMatch.PRIVE) {
            match.getJoueurs().add(organisateur);
        }
        
        return matchRepository.save(match);
    }

    public Match ajouterJoueurMatchPrive(Long matchId, String matriculeJoueur) {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new RuntimeException("Match non trouvé"));
        
        if (match.getTypeMatch() != TypeMatch.PRIVE) {
            throw new RuntimeException("Ce match n'est pas privé");
        }
        
        if (match.getJoueurs().size() >= NOMBRE_JOUEURS_REQUIS) {
            throw new RuntimeException("Le match est déjà complet");
        }
        
        Membre joueur = membreRepository.findByMatricule(matriculeJoueur)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        if (match.getJoueurs().contains(joueur)) {
            throw new RuntimeException("Le joueur est déjà dans ce match");
        }
        
        match.getJoueurs().add(joueur);
        
        if (match.getJoueurs().size() == NOMBRE_JOUEURS_REQUIS) {
            match.setEstComplet(true);
        }
        
        return matchRepository.save(match);
    }

    public Paiement effectuerPaiement(Long matchId, String matricule) {
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new RuntimeException("Match non trouvé"));
        
        Membre membre = membreRepository.findByMatricule(matricule)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        // Vérifier si le paiement existe déjà
        Optional<Paiement> paiementExistant = paiementRepository.findByMatchIdAndMembreId(matchId, membre.getId());
        if (paiementExistant.isPresent()) {
            throw new RuntimeException("Paiement déjà effectué pour ce match");
        }
        
        // Si match public, vérifier qu'il y a de la place
        if (match.getTypeMatch() == TypeMatch.PUBLIC) {
            if (match.getJoueurs().size() >= NOMBRE_JOUEURS_REQUIS) {
                throw new RuntimeException("Le match est complet");
            }
            // Ajouter le joueur au match
            if (!match.getJoueurs().contains(membre)) {
                match.getJoueurs().add(membre);
            }
        }
        
        // Si match privé, vérifier que le membre est dans la liste
        if (match.getTypeMatch() == TypeMatch.PRIVE) {
            if (!match.getJoueurs().contains(membre)) {
                throw new RuntimeException("Vous n'êtes pas autorisé à payer pour ce match privé");
            }
        }
        
        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setMatch(match);
        paiement.setMembre(membre);
        paiement.setMontant(PRIX_PAR_JOUEUR);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setEstValide(true);
        
        paiement = paiementRepository.save(paiement);
        
        // Vérifier si le match est maintenant complet et payé
        verifierCompletudeMatch(match);
        
        // Si le membre a un solde dû, l'ajouter au paiement
        if (membre.getSoldeDu() > 0) {
            membre.setSoldeDu(0.0);
            membreRepository.save(membre);
        }
        
        return paiement;
    }

    private void verifierCompletudeMatch(Match match) {
        // Compter les paiements valides
        Double totalPaiements = paiementRepository.getTotalPaiementsByMatchId(match.getId());
        if (totalPaiements == null) totalPaiements = 0.0;
        
        // Vérifier si tous les joueurs ont payé
        if (match.getJoueurs().size() == NOMBRE_JOUEURS_REQUIS && 
            totalPaiements >= match.getPrixTotal()) {
            match.setEstComplet(true);
            match.setEstPaye(true);
        } else if (match.getJoueurs().size() == NOMBRE_JOUEURS_REQUIS) {
            match.setEstComplet(true);
        }
        
        matchRepository.save(match);
    }

    // Tâche planifiée : vérifier les matches privés la veille
    @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    public void verifierMatchesPrives() {
        LocalDate demain = LocalDate.now().plusDays(1);
        List<Match> matchesDemain = matchRepository.findByDate(demain);
        
        for (Match match : matchesDemain) {
            if (match.getTypeMatch() == TypeMatch.PRIVE && !match.getEstComplet()) {
                // Convertir en public
                match.setTypeMatch(TypeMatch.PUBLIC);
                match.setDateConversionPublic(LocalDateTime.now());
                
                // Appliquer pénalité à l'organisateur
                Membre organisateur = match.getOrganisateur();
                organisateur.setSoldeDu(organisateur.getSoldeDu() + match.getPrixTotal());
                membreRepository.save(organisateur);
                
                matchRepository.save(match);
            }
        }
    }

    // Tâche planifiée : vérifier les paiements la veille
    @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    public void verifierPaiements() {
        LocalDate demain = LocalDate.now().plusDays(1);
        List<Match> matchesDemain = matchRepository.findByDate(demain);
        
        for (Match match : matchesDemain) {
            if (!match.getEstPaye()) {
                // Vérifier les joueurs qui n'ont pas payé
                for (Membre joueur : match.getJoueurs()) {
                    Optional<Paiement> paiement = paiementRepository.findByMatchIdAndMembreId(match.getId(), joueur.getId());
                    if (paiement.isEmpty() || !paiement.get().getEstValide()) {
                        // Retirer le joueur du match et rendre la place disponible
                        match.getJoueurs().remove(joueur);
                        match.setEstComplet(false);
                        
                        // Si match privé, appliquer pénalité
                        if (match.getTypeMatch() == TypeMatch.PRIVE) {
                            joueur.setSoldeDu(joueur.getSoldeDu() + match.getPrixParJoueur());
                            membreRepository.save(joueur);
                        }
                    }
                }
                
                // Si match public et incomplet, l'organisateur doit payer le solde
                if (match.getTypeMatch() == TypeMatch.PUBLIC && !match.getEstComplet()) {
                    Double totalPaiements = paiementRepository.getTotalPaiementsByMatchId(match.getId());
                    if (totalPaiements == null) totalPaiements = 0.0;
                    
                    Double solde = match.getPrixTotal() - totalPaiements;
                    if (solde > 0) {
                        Membre organisateur = match.getOrganisateur();
                        organisateur.setSoldeDu(organisateur.getSoldeDu() + solde);
                        membreRepository.save(organisateur);
                    }
                }
                
                matchRepository.save(match);
            }
        }
    }

    private void validateDelaiReservation(Membre membre, LocalDate dateMatch) {
        LocalDate aujourdhui = LocalDate.now();
        long joursAvant = java.time.temporal.ChronoUnit.DAYS.between(aujourdhui, dateMatch);
        
        switch (membre.getTypeMembre()) {
            case GLOBAL:
                if (joursAvant < 21) { // 3 semaines
                    throw new RuntimeException("Les membres globaux doivent réserver au moins 3 semaines à l'avance");
                }
                break;
            case SITE:
                if (joursAvant < 14) { // 2 semaines
                    throw new RuntimeException("Les membres de site doivent réserver au moins 2 semaines à l'avance");
                }
                break;
            case LIBRE:
                if (joursAvant < 5) {
                    throw new RuntimeException("Les membres libres doivent réserver au moins 5 jours à l'avance");
                }
                break;
        }
    }

    private void validateDisponibiliteTerrain(Terrain terrain, LocalDate date, LocalTime heureDebut) {
        Site site = terrain.getSite();
        LocalTime heureFin = heureDebut.plusMinutes(DUREE_MATCH_MINUTES);
        
        // Vérifier les horaires du site
        if (heureDebut.isBefore(site.getHeureDebut()) || heureFin.isAfter(site.getHeureFin())) {
            throw new RuntimeException("L'heure de réservation est en dehors des horaires du site");
        }
        
        // Vérifier les conflits avec d'autres matches
        List<Match> matchesExistant = matchRepository.findByDate(date);
        for (Match match : matchesExistant) {
            if (match.getTerrain().getId().equals(terrain.getId())) {
                LocalTime finMatch = match.getHeureFin().plusMinutes(DUREE_ENTRE_MATCH_MINUTES);
                if ((heureDebut.isBefore(match.getHeureFin()) && heureFin.isAfter(match.getHeureDebut())) ||
                    (heureDebut.isBefore(finMatch) && heureFin.isAfter(match.getHeureDebut()))) {
                    throw new RuntimeException("Le terrain n'est pas disponible à cette heure");
                }
            }
        }
    }

    private void validateJourFermeture(Site site, LocalDate date) {
        // Vérifier les fermetures globales
        List<JourFermeture> fermeturesGlobales = jourFermetureRepository.findBySiteIsNull();
        for (JourFermeture jf : fermeturesGlobales) {
            if (jf.getDate().equals(date)) {
                throw new RuntimeException("Jour de fermeture globale");
            }
        }
        
        // Vérifier les fermetures du site
        List<JourFermeture> fermeturesSite = jourFermetureRepository.findBySiteId(site.getId());
        for (JourFermeture jf : fermeturesSite) {
            if (jf.getDate().equals(date)) {
                throw new RuntimeException("Jour de fermeture du site");
            }
        }
    }
}

