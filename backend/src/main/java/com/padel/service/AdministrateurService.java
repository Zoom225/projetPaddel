package com.padel.service;

import com.padel.entity.Administrateur;
import com.padel.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministrateurService {
    private final AdministrateurRepository administrateurRepository;

    public List<Administrateur> getAllAdministrateurs() {
        return administrateurRepository.findAll();
    }

    public Optional<Administrateur> getAdministrateurById(Long id) {
        return administrateurRepository.findById(id);
    }

    public Optional<Administrateur> getAdministrateurByMatricule(String matricule) {
        return administrateurRepository.findByMatricule(matricule);
    }

    public Optional<Administrateur> getAdministrateurByEmail(String email) {
        return administrateurRepository.findByEmail(email);
    }

    public List<Administrateur> getAdministrateursBySiteId(Long siteId) {
        return administrateurRepository.findBySiteId(siteId);
    }

    public Administrateur createAdministrateur(Administrateur administrateur) {
        return administrateurRepository.save(administrateur);
    }

    public Administrateur updateAdministrateur(Long id, Administrateur administrateurDetails) {
        return administrateurRepository.findById(id).map(admin -> {
            admin.setNom(administrateurDetails.getNom());
            admin.setPrenom(administrateurDetails.getPrenom());
            admin.setEmail(administrateurDetails.getEmail());
            admin.setTypeAdministrateur(administrateurDetails.getTypeAdministrateur());
            return administrateurRepository.save(admin);
        }).orElseThrow(() -> new RuntimeException("Administrateur not found"));
    }

    public void deleteAdministrateur(Long id) {
        administrateurRepository.deleteById(id);
    }
}

