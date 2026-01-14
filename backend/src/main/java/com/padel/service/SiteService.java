package com.padel.service;

import com.padel.model.Site;
import com.padel.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SiteService {
    
    @Autowired
    private SiteRepository siteRepository;

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public Optional<Site> getSiteById(Long id) {
        return siteRepository.findById(id);
    }

    public Site createSite(Site site) {
        return siteRepository.save(site);
    }

    public Site updateSite(Long id, Site siteDetails) {
        Site site = siteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Site non trouvé"));
        
        site.setNom(siteDetails.getNom());
        site.setHeureDebut(siteDetails.getHeureDebut());
        site.setHeureFin(siteDetails.getHeureFin());
        site.setDureeMatchMinutes(siteDetails.getDureeMatchMinutes());
        site.setDureeEntreMatchMinutes(siteDetails.getDureeEntreMatchMinutes());
        
        return siteRepository.save(site);
    }

    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}

