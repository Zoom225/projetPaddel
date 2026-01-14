package com.padel.service;

import com.padel.model.Membre;
import com.padel.model.TypeMembre;
import com.padel.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembreService {
    
    @Autowired
    private MembreRepository membreRepository;

    public List<Membre> getAllMembres() {
        return membreRepository.findAll();
    }

    public Optional<Membre> getMembreByMatricule(String matricule) {
        return membreRepository.findByMatricule(matricule);
    }

    public Optional<Membre> getMembreById(Long id) {
        return membreRepository.findById(id);
    }

    public Membre createMembre(Membre membre) {
        validateMatricule(membre.getMatricule(), membre.getTypeMembre());
        return membreRepository.save(membre);
    }

    public Membre updateMembre(Long id, Membre membreDetails) {
        Membre membre = membreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        membre.setNom(membreDetails.getNom());
        membre.setPrenom(membreDetails.getPrenom());
        membre.setEmail(membreDetails.getEmail());
        
        return membreRepository.save(membre);
    }

    public void deleteMembre(Long id) {
        membreRepository.deleteById(id);
    }

    public List<Membre> getMembresByType(TypeMembre type) {
        return membreRepository.findByTypeMembre(type);
    }

    public List<Membre> getMembresBySite(Long siteId) {
        return membreRepository.findBySiteId(siteId);
    }

    private void validateMatricule(String matricule, TypeMembre type) {
        if (type == TypeMembre.GLOBAL && !matricule.startsWith("G")) {
            throw new IllegalArgumentException("Le matricule d'un membre global doit commencer par G");
        }
        if (type == TypeMembre.SITE && !matricule.startsWith("S")) {
            throw new IllegalArgumentException("Le matricule d'un membre de site doit commencer par S");
        }
        if (type == TypeMembre.LIBRE && !matricule.startsWith("L")) {
            throw new IllegalArgumentException("Le matricule d'un membre libre doit commencer par L");
        }
    }
}

