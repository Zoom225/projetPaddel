package com.padel.service;

import com.padel.entity.Membre;
import com.padel.repository.MembreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembreService {
    private final MembreRepository membreRepository;

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

    public Optional<Membre> getMembreById(Long id) {
        return membreRepository.findById(id);
    }

    public Optional<Membre> getMembreByMatricule(String matricule) {
        return membreRepository.findByMatricule(matricule);
    }

    public Optional<Membre> getMembreByEmail(String email) {
        return membreRepository.findByEmail(email);
    }

    public List<Membre> getMembresBySiteId(Long siteId) {
        return membreRepository.findBySiteId(siteId);
    }

    public Membre createMembre(Membre membre) {
        return membreRepository.save(membre);
    }

    public Membre updateMembre(Long id, Membre membreDetails) {
        return membreRepository.findById(id).map(membre -> {
            membre.setNom(membreDetails.getNom());
            membre.setPrenom(membreDetails.getPrenom());
            membre.setEmail(membreDetails.getEmail());
            membre.setTypeMembre(membreDetails.getTypeMembre());
            membre.setSoldeDu(membreDetails.getSoldeDu());
            return membreRepository.save(membre);
        }).orElseThrow(() -> new RuntimeException("Membre not found"));
    }

    public void deleteMembre(Long id) {
        membreRepository.deleteById(id);
    }
}

