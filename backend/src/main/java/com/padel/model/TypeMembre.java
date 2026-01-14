package com.padel.model;

public enum TypeMembre {
    GLOBAL,      // Gxxxx - peut réserver 3 semaines avant, sur tous les sites
    SITE,        // Sxxxxx - peut réserver 2 semaines avant, uniquement sur son site
    LIBRE        // Lxxxxx - peut réserver 5 jours avant, sur tous les sites
}

