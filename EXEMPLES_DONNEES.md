# Exemples de Données et Utilisation

Ce document explique où et comment les données d'exemple ont été créées, et comment les utiliser.

## 📍 Emplacement des Données

### Fichier Principal : `DataInitializer.java`
**Chemin :** `backend/src/main/java/com/padel/config/DataInitializer.java`

Ce fichier contient toute la logique d'initialisation des données d'exemple. Il s'exécute automatiquement au démarrage de l'application Spring Boot si la base de données est vide.

---

## 📊 Données Créées

### 1. Sites (3 sites)

| Site | Horaires | Terrains |
|------|----------|----------|
| **Site Paris Centre** | 8h00 - 22h00 | 3 terrains |
| **Site Lyon Nord** | 9h00 - 21h00 | 2 terrains |
| **Site Marseille Sud** | 7h30 - 23h00 | 2 terrains |

**Total : 7 terrains**

### 2. Membres (7 membres)

| Matricule | Nom | Prénom | Type | Site |
|-----------|-----|--------|------|------|
| G0001 | Martin | Pierre | GLOBAL | Tous |
| G0002 | Dupont | Sophie | GLOBAL | Tous |
| S00001 | Dubois | Marie | SITE | Paris Centre |
| S00002 | Lefebvre | Thomas | SITE | Lyon Nord |
| L00001 | Bernard | Jean | LIBRE | - |
| L00002 | Moreau | Claire | LIBRE | - |
| L00003 | Petit | Lucas | LIBRE | - |

### 3. Matches (6 matches avec tous les détails)

#### Match 1 : Match PRIVÉ COMPLET avec PAIEMENTS ✅
- **Date** : Aujourd'hui
- **Terrain** : Paris Centre - Terrain 1
- **Type** : PRIVE
- **Joueurs** : Pierre Martin, Sophie Dupont, Marie Dubois, Jean Bernard (4/4)
- **Statut** : ✅ Complet et ✅ Payé
- **Paiement** : 4 paiements de 15€ chacun (60€ total)

#### Match 2 : Match PUBLIC INCOMPLET avec PAIEMENTS PARTIELS ⚠️
- **Date** : Aujourd'hui
- **Terrain** : Paris Centre - Terrain 2
- **Type** : PUBLIC
- **Joueurs** : Marie Dubois, Claire Moreau (2/4)
- **Statut** : ❌ Incomplet, ⚠️ Paiements partiels (2 paiements)
- **Paiement** : 2 paiements de 15€ (30€ sur 60€)

#### Match 3 : Match PRIVÉ COMPLET SANS PAIEMENT ⚠️
- **Date** : Demain
- **Terrain** : Lyon Nord - Terrain 1
- **Type** : PRIVE
- **Joueurs** : Sophie Dupont, Thomas Lefebvre, Jean Bernard, Lucas Petit (4/4)
- **Statut** : ✅ Complet mais ❌ Non payé
- **Paiement** : Aucun paiement (0€)

#### Match 4 : Match PUBLIC COMPLET avec PAIEMENTS ✅
- **Date** : Demain
- **Terrain** : Marseille Sud - Terrain 1
- **Type** : PUBLIC
- **Joueurs** : Lucas Petit, Pierre Martin, Marie Dubois, Claire Moreau (4/4)
- **Statut** : ✅ Complet et ✅ Payé
- **Paiement** : 4 paiements de 15€ chacun (60€ total)

#### Match 5 : Match PRIVÉ INCOMPLET (en attente) ⏳
- **Date** : Après-demain
- **Terrain** : Paris Centre - Terrain 3
- **Type** : PRIVE
- **Joueurs** : Pierre Martin, Jean Bernard (2/4)
- **Statut** : ❌ Incomplet, ❌ Non payé
- **Paiement** : Aucun paiement

#### Match 6 : Match PUBLIC avec un seul joueur (ouvert) 🔓
- **Date** : Après-demain
- **Terrain** : Lyon Nord - Terrain 2
- **Type** : PUBLIC
- **Joueurs** : Thomas Lefebvre (1/4)
- **Statut** : ❌ Très incomplet, ❌ Non payé
- **Paiement** : Aucun paiement

---

## 🔌 Exemple de Connexion et Utilisation

### 1. Accéder aux Données via l'API REST

Une fois le backend lancé, vous pouvez accéder aux données via les endpoints suivants :

#### Récupérer tous les sites
```bash
GET http://localhost:8080/api/sites
```

#### Récupérer tous les matches
```bash
GET http://localhost:8080/api/matches
```

#### Récupérer un match spécifique
```bash
GET http://localhost:8080/api/matches/1
```

#### Récupérer les matches d'un site
```bash
GET http://localhost:8080/api/sites/1/matches
```

### 2. Exemple de Code Java (Backend)

**Fichier :** `backend/src/main/java/com/padel/service/MatchService.java`

```java
// Exemple : Récupérer tous les matches avec leurs détails
@Autowired
private MatchRepository matchRepository;

public List<Match> getAllMatches() {
    return matchRepository.findAll();
}

// Exemple : Récupérer les matches complets et payés
public List<Match> getMatchesCompletsEtPayes() {
    return matchRepository.findAll().stream()
        .filter(m -> m.getEstComplet() && m.getEstPaye())
        .collect(Collectors.toList());
}
```

### 3. Exemple de Code TypeScript (Frontend)

**Fichier :** `frontend/src/app/services/api.service.ts`

```typescript
// Exemple : Récupérer tous les matches
getAllMatches(): Observable<Match[]> {
  return this.http.get<Match[]>(`${this.apiUrl}/matches`);
}

// Exemple : Afficher les détails d'un match
getMatchDetails(matchId: number): Observable<Match> {
  return this.http.get<Match>(`${this.apiUrl}/matches/${matchId}`);
}
```

### 4. Exemple d'Utilisation dans le Frontend

**Fichier :** `frontend/src/app/components/matches-publics/matches-publics.component.ts`

```typescript
// Charger les matches publics
ngOnInit() {
  this.apiService.getAllMatches().subscribe(matches => {
    // Filtrer les matches publics
    this.matchesPublics = matches.filter(m => m.typeMatch === 'PUBLIC');
    
    // Afficher les détails
    this.matchesPublics.forEach(match => {
      console.log(`Match ${match.id}:`);
      console.log(`  Date: ${match.date}`);
      console.log(`  Terrain: ${match.terrain.nom}`);
      console.log(`  Type: ${match.typeMatch}`);
      console.log(`  Joueurs: ${match.joueurs.length}/4`);
      console.log(`  Statut: ${match.estComplet ? 'Complet' : 'Incomplet'}`);
      console.log(`  Paiement: ${match.estPaye ? 'Payé' : 'Non payé'}`);
    });
  });
}
```

---

## 📋 Structure des Données dans la Base

### Table `matches`
| Colonne | Type | Description | Exemple |
|---------|------|-------------|---------|
| `id` | Long | Identifiant unique | 1 |
| `date` | LocalDate | Date du match | 2024-01-15 |
| `heure_debut` | LocalTime | Heure de début | 10:00 |
| `heure_fin` | LocalTime | Heure de fin | 11:30 |
| `type_match` | Enum | PRIVE ou PUBLIC | PRIVE |
| `est_complet` | Boolean | 4 joueurs présents ? | true |
| `est_paye` | Boolean | Tous les paiements effectués ? | true |
| `prix_total` | Double | Prix total du match | 60.0 |
| `prix_par_joueur` | Double | Prix par joueur | 15.0 |

### Table `match_joueurs` (relation Many-to-Many)
| match_id | membre_id |
|----------|-----------|
| 1 | 1 |
| 1 | 2 |
| 1 | 3 |
| 1 | 5 |

### Table `paiements`
| Colonne | Type | Description | Exemple |
|---------|------|-------------|---------|
| `id` | Long | Identifiant unique | 1 |
| `match_id` | Long | Référence au match | 1 |
| `membre_id` | Long | Référence au membre | 1 |
| `montant` | Double | Montant payé | 15.0 |
| `date_paiement` | LocalDateTime | Date du paiement | 2024-01-13 10:30 |
| `est_valide` | Boolean | Paiement valide ? | true |

---

## 🎯 Cas d'Usage avec les Données d'Exemple

### Cas 1 : Voir tous les matches disponibles aujourd'hui
```bash
curl http://localhost:8080/api/matches?date=2024-01-15
```

**Résultat attendu :** Match 1 et Match 2

### Cas 2 : Voir les matches publics incomplets (où on peut s'inscrire)
```bash
# Via l'interface web : http://localhost:4200/matches-publics
```

**Résultat attendu :** Match 2, Match 6

### Cas 3 : Voir les matches d'un membre spécifique
```bash
GET http://localhost:8080/api/membres/G0001/matches
```

**Résultat attendu :** Match 1, Match 4, Match 5

### Cas 4 : Vérifier les paiements d'un match
```bash
GET http://localhost:8080/api/matches/1/paiements
```

**Résultat attendu :** 4 paiements de 15€ chacun

---

## 🔍 Où Trouver les Données dans le Code

### Backend

1. **Initialisation des données :**
   - `backend/src/main/java/com/padel/config/DataInitializer.java`
   - S'exécute au démarrage si la base est vide

2. **Modèles de données :**
   - `backend/src/main/java/com/padel/model/Match.java` - Modèle Match
   - `backend/src/main/java/com/padel/model/Site.java` - Modèle Site
   - `backend/src/main/java/com/padel/model/Membre.java` - Modèle Membre
   - `backend/src/main/java/com/padel/model/Paiement.java` - Modèle Paiement

3. **Repositories (accès aux données) :**
   - `backend/src/main/java/com/padel/repository/MatchRepository.java`
   - `backend/src/main/java/com/padel/repository/SiteRepository.java`
   - `backend/src/main/java/com/padel/repository/MembreRepository.java`
   - `backend/src/main/java/com/padel/repository/PaiementRepository.java`

4. **Controllers (API REST) :**
   - `backend/src/main/java/com/padel/controller/MatchController.java`
   - `backend/src/main/java/com/padel/controller/SiteController.java`
   - `backend/src/main/java/com/padel/controller/MembreController.java`

### Frontend

1. **Service API :**
   - `frontend/src/app/services/api.service.ts` - Communication avec le backend

2. **Composants :**
   - `frontend/src/app/components/matches-publics/matches-publics.component.ts` - Affichage des matches publics
   - `frontend/src/app/components/mes-reservations/mes-reservations.component.ts` - Réservations d'un membre

---

## 📝 Commentaires dans le Code

Tous les commentaires sont en **français** et expliquent :
- ✅ Ce que fait chaque section
- ✅ Les détails de chaque match créé
- ✅ Les relations entre les entités
- ✅ Les statuts et paiements

**Exemple de commentaire dans DataInitializer.java :**
```java
// ============================================================
// MATCH 1 : Match PRIVÉ COMPLET avec PAIEMENTS
// Date: Aujourd'hui, Terrain: Paris 1, Type: PRIVE, Statut: Complet et Payé
// ============================================================
```

---

## 🚀 Pour Tester les Données

1. **Lancez le backend :**
   ```bash
   cd backend
   mvn spring-boot:run
   # ou
   demarrer-backend.bat
   ```

2. **Accédez à PostgreSQL (base de données) :**
   La base est lancée par Docker (`docker-compose.yml`).
   - Host : `localhost`
   - Port : `5432`
   - DB : `padelService`
   - User : `padel`
   - Password : `padel`

   Exemple (psql) :
   ```bash
   psql -h localhost -p 5432 -U padel -d padelService
   ```

3. **Requêtes SQL pour voir les données :**
   ```sql
   -- Voir tous les matches
   SELECT * FROM matches;
   
   -- Voir les matches avec leurs terrains
   SELECT m.id, m.date, m.type_match, m.est_complet, m.est_paye, t.nom as terrain
   FROM matches m
   JOIN terrains t ON m.terrain_id = t.id;
   
   -- Voir les paiements
   SELECT * FROM paiements;
   
   -- Voir les joueurs d'un match
   SELECT m.nom, m.prenom, mj.match_id
   FROM membres m
   JOIN match_joueurs mj ON m.id = mj.membre_id
   WHERE mj.match_id = 1;
   ```

4. **Testez via l'API :**
   ```bash
   # Liste des sites
   curl http://localhost:8080/api/sites
   
   # Liste des matches
   curl http://localhost:8080/api/matches
   
   # Détails d'un match
   curl http://localhost:8080/api/matches/1
   ```

---

## ✅ Résumé

- **3 sites** créés avec **7 terrains** au total
- **7 membres** de différents types
- **6 matches** avec différents statuts :
  - ✅ 2 matches complets et payés
  - ⚠️ 1 match complet mais non payé
  - ⚠️ 1 match incomplet avec paiements partiels
  - ❌ 2 matches incomplets sans paiements
- **Paiements** associés aux matches (10 paiements au total)

Toutes les données sont créées automatiquement au premier démarrage de l'application !

