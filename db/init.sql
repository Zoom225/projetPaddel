-- Initialisation optionnelle PostgreSQL pour le projet "padelService"
-- Exécution automatique par l'image Postgres (docker-entrypoint-initdb.d) au 1er démarrage du volume.
-- Remarque : le backend Spring Boot peut aussi créer/mettre à jour les tables via JPA (ddl-auto).

-- ======================
-- Schéma (DDL)
-- ======================

CREATE TABLE IF NOT EXISTS sites (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(255) NOT NULL UNIQUE,
  heure_debut TIME NOT NULL,
  heure_fin TIME NOT NULL,
  duree_match_minutes INTEGER NOT NULL DEFAULT 90,
  duree_entre_match_minutes INTEGER NOT NULL DEFAULT 15
);

CREATE TABLE IF NOT EXISTS terrains (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(255) NOT NULL,
  site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS jours_fermeture (
  id BIGSERIAL PRIMARY KEY,
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE CASCADE,
  date DATE NOT NULL,
  raison VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS administrateurs (
  id BIGSERIAL PRIMARY KEY,
  matricule VARCHAR(255) NOT NULL UNIQUE,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  type_administrateur VARCHAR(32) NOT NULL,
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS membres (
  id BIGSERIAL PRIMARY KEY,
  matricule VARCHAR(255) NOT NULL UNIQUE,
  nom VARCHAR(255) NOT NULL,
  prenom VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  type_membre VARCHAR(32) NOT NULL,
  site_id BIGINT NULL REFERENCES sites(id) ON DELETE SET NULL,
  solde_du DOUBLE PRECISION NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS matches (
  id BIGSERIAL PRIMARY KEY,
  terrain_id BIGINT NOT NULL REFERENCES terrains(id) ON DELETE CASCADE,
  date DATE NOT NULL,
  heure_debut TIME NOT NULL,
  heure_fin TIME NOT NULL,
  type_match VARCHAR(32) NOT NULL,
  organisateur_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  prix_total DOUBLE PRECISION NOT NULL DEFAULT 60,
  prix_par_joueur DOUBLE PRECISION NOT NULL DEFAULT 15,
  est_complet BOOLEAN NOT NULL DEFAULT FALSE,
  est_paye BOOLEAN NOT NULL DEFAULT FALSE,
  date_creation TIMESTAMP NOT NULL,
  date_conversion_public TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS match_joueurs (
  match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  membre_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  PRIMARY KEY (match_id, membre_id)
);

CREATE TABLE IF NOT EXISTS paiements (
  id BIGSERIAL PRIMARY KEY,
  match_id BIGINT NOT NULL REFERENCES matches(id) ON DELETE CASCADE,
  membre_id BIGINT NOT NULL REFERENCES membres(id) ON DELETE CASCADE,
  montant DOUBLE PRECISION NOT NULL,
  date_paiement TIMESTAMP NOT NULL,
  est_valide BOOLEAN NOT NULL DEFAULT TRUE
);

-- ======================
-- Données de base (seed léger)
-- ======================
-- (Les admins et données complètes sont aussi seedés par le backend au démarrage via DataInitializer.)

INSERT INTO sites (nom, heure_debut, heure_fin, duree_match_minutes, duree_entre_match_minutes)
VALUES
  ('Bruxelles', '08:00:00', '22:00:00', 90, 15),
  ('Paris', '09:00:00', '23:00:00', 90, 15)
ON CONFLICT (nom) DO NOTHING;

-- Terrains (si inexistants) : on évite les doublons par une condition NOT EXISTS.
INSERT INTO terrains (nom, site_id)
SELECT 'Terrain A', s.id FROM sites s
WHERE s.nom = 'Bruxelles'
  AND NOT EXISTS (
    SELECT 1 FROM terrains t WHERE t.nom = 'Terrain A' AND t.site_id = s.id
  );

INSERT INTO terrains (nom, site_id)
SELECT 'Terrain B', s.id FROM sites s
WHERE s.nom = 'Bruxelles'
  AND NOT EXISTS (
    SELECT 1 FROM terrains t WHERE t.nom = 'Terrain B' AND t.site_id = s.id
  );

INSERT INTO terrains (nom, site_id)
SELECT 'Terrain 1', s.id FROM sites s
WHERE s.nom = 'Paris'
  AND NOT EXISTS (
    SELECT 1 FROM terrains t WHERE t.nom = 'Terrain 1' AND t.site_id = s.id
  );
