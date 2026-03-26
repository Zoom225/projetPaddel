-- ====================================================================================
-- INITIALISATION DE LA BASE DE DONNÉES "padelService"
-- ====================================================================================
-- Ce script est exécuté automatiquement par Docker au premier lancement (si la base est vide).
-- Il garantit que le schéma est parfaitement aligné avec les entités JPA du backend.

-- 1. Table SITES
CREATE TABLE IF NOT EXISTS sites (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(255) NOT NULL UNIQUE,
  heure_debut TIME NOT NULL,
  heure_fin TIME NOT NULL,
  duree_match_minutes INTEGER NOT NULL DEFAULT 90,
  duree_entre_match_minutes INTEGER NOT NULL DEFAULT 15
);

-- 2. Table TERRAINS
CREATE TABLE IF NOT EXISTS terrains (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
  UNIQUE(nom, site_id) -- Un nom de terrain doit être unique par site
);

-- 3. Table JOURS_FERMETURE
CREATE TABLE IF NOT EXISTS jours_fermeture (
  id BIGSERIAL PRIMARY KEY,
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE CASCADE,
  date DATE NOT NULL,
  raison VARCHAR(255)
);

-- 4. Table ADMINISTRATEURS (Utilisateurs avec Login)
-- Types: GLOBAL, SITE
CREATE TABLE IF NOT EXISTS administrateurs (
  id BIGSERIAL PRIMARY KEY,
  matricule VARCHAR(255) NOT NULL UNIQUE,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL, -- Mot de passe crypté (BCrypt)
  type_administrateur VARCHAR(32) NOT NULL, -- 'GLOBAL' ou 'SITE'
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE SET NULL,
  CONSTRAINT chk_admin_site CHECK (
    (type_administrateur = 'GLOBAL' AND site_id IS NULL) OR
    (type_administrateur = 'SITE' AND site_id IS NOT NULL)
  ),
  CONSTRAINT chk_type_administrateur CHECK (type_administrateur IN ('GLOBAL', 'SITE'))
);

-- 5. Table MEMBRES (Utilisateurs sans Login, par Matricule)
-- Types: GLOBAL, SITE, LIBRE
CREATE TABLE IF NOT EXISTS membres (
  id BIGSERIAL PRIMARY KEY,
  matricule VARCHAR(255) NOT NULL UNIQUE,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  type_membre VARCHAR(32) NOT NULL, -- 'GLOBAL', 'SITE', 'LIBRE'
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE SET NULL,
  solde_du DOUBLE PRECISION NOT NULL DEFAULT 0,
  CONSTRAINT chk_membre_site CHECK (
    (type_membre IN ('GLOBAL', 'LIBRE') AND site_id IS NULL) OR
    (type_membre = 'SITE' AND site_id IS NOT NULL)
  ),
  CONSTRAINT chk_type_membre CHECK (type_membre IN ('GLOBAL', 'SITE', 'LIBRE'))
);

-- 6. Table MATCHES
CREATE TABLE IF NOT EXISTS matches (
  id BIGSERIAL PRIMARY KEY,
  terrain_id BIGINT NOT NULL REFERENCES terrains(id) ON DELETE CASCADE,
  date DATE NOT NULL,
  heure_debut TIME NOT NULL,
  heure_fin TIME NOT NULL,
  type_match VARCHAR(32) NOT NULL, -- 'PRIVE', 'PUBLIC'
  organisateur_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  prix_total DOUBLE PRECISION NOT NULL DEFAULT 60,
  prix_par_joueur DOUBLE PRECISION NOT NULL DEFAULT 15,
  est_complet BOOLEAN NOT NULL DEFAULT FALSE,
  est_paye BOOLEAN NOT NULL DEFAULT FALSE,
  date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_conversion_public TIMESTAMP NULL,
  CONSTRAINT chk_type_match CHECK (type_match IN ('PRIVE', 'PUBLIC'))
);

-- 7. Table MATCH_JOUEURS (Table de jointure Match <-> Membres)
CREATE TABLE IF NOT EXISTS match_joueurs (
  match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  membre_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  PRIMARY KEY (match_id, membre_id)
);

-- 8. Table PAIEMENTS
CREATE TABLE IF NOT EXISTS paiements (
  id BIGSERIAL PRIMARY KEY,
  match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  membre_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  montant DOUBLE PRECISION NOT NULL,
  date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  est_valide BOOLEAN NOT NULL DEFAULT TRUE
);

-- ====================================================================================
-- DONNÉES DE DÉMARRAGE (SEED)
-- ====================================================================================
-- Ces données sont insérées si les tables sont vides.
-- Note: Le backend Java (DataInitializer.java) contient aussi un seed complet. 
-- Ce SQL est une sécurité supplémentaire.

-- Sites
INSERT INTO sites (nom, heure_debut, heure_fin) VALUES 
('Site Paris Centre', '08:00:00', '22:00:00'),
('Site Lyon Nord', '09:00:00', '21:00:00'),
('Site Marseille Sud', '07:30:00', '23:00:00')
ON CONFLICT (nom) DO NOTHING;

-- Terrains (Paris)
INSERT INTO terrains (nom, site_id)
SELECT 'Terrain 1', id FROM sites WHERE nom = 'Site Paris Centre'
ON CONFLICT (nom, site_id) DO NOTHING;

INSERT INTO terrains (nom, site_id)
SELECT 'Terrain 2', id FROM sites WHERE nom = 'Site Paris Centre'
ON CONFLICT (nom, site_id) DO NOTHING;

-- Terrains (Lyon)
INSERT INTO terrains (nom, site_id)
SELECT 'Terrain A', id FROM sites WHERE nom = 'Site Lyon Nord'
ON CONFLICT (nom, site_id) DO NOTHING;
