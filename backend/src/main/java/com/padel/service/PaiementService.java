package com.padel.service;

import com.padel.entity.Match;
import com.padel.entity.Membre;
import com.padel.entity.Paiement;
import com.padel.repository.MatchRepository;
import com.padel.repository.MembreRepository;
import com.padel.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaiementService {
    private final PaiementRepository paiementRepository;
    private final MatchRepository matchRepository;
    private final MembreRepository membreRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public List<Paiement> getPaiementsByMatchId(Long matchId) {
        return paiementRepository.findByMatchId(matchId);
    }

    public List<Paiement> getPaiementsByMembreId(Long membreId) {
        return paiementRepository.findByMembreId(membreId);
    }

    public Paiement createPaiement(Long matchId, String matricule) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        Membre membre = membreRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Membre not found"));

        Paiement paiement = Paiement.builder()
                .match(match)
                .membre(membre)
                .montant(match.getPrixParJoueur())
                .build();

        return paiementRepository.save(paiement);
    }

    public Paiement updatePaiement(Long id, Paiement paiementDetails) {
        return paiementRepository.findById(id).map(paiement -> {
            paiement.setMontant(paiementDetails.getMontant());
            paiement.setEstValide(paiementDetails.getEstValide());
            return paiementRepository.save(paiement);
        }).orElseThrow(() -> new RuntimeException("Paiement not found"));
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}
