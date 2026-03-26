package com.padel.service;

import com.padel.entity.Site;
import com.padel.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteService {
    private final SiteRepository siteRepository;

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
        return siteRepository.findById(id).map(site -> {
            site.setNom(siteDetails.getNom());
            site.setHeureDebut(siteDetails.getHeureDebut());
            site.setHeureFin(siteDetails.getHeureFin());
            site.setDureeMatchMinutes(siteDetails.getDureeMatchMinutes());
            site.setDureeEntreMatchMinutes(siteDetails.getDureeEntreMatchMinutes());
            return siteRepository.save(site);
        }).orElseThrow(() -> new RuntimeException("Site not found"));
    }

    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}

