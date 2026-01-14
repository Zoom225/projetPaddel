# 🚀 Guide de Démarrage Rapide

## Méthode la Plus Simple (Recommandée)

### Option 1 : Lancer Tout en Une Fois ⚡

**📍 Où se trouve le fichier ?**

Le fichier `demarrer-tout.bat` se trouve à la **racine du projet** :
```
C:\Users\user\Desktop\padelService_projet_2\demarrer-tout.bat
```

**🚀 Comment l'utiliser ?**

**Méthode 1 : Double-clic (le plus simple)**
1. Ouvrez l'Explorateur de fichiers Windows
2. Naviguez jusqu'à : `C:\Users\user\Desktop\padelService_projet_2`
3. Double-cliquez sur **`demarrer-tout.bat`**

**Méthode 2 : Depuis PowerShell/CMD**
```powershell
# Allez à la racine du projet
cd C:\Users\user\Desktop\padelService_projet_2

# Lancez le script
.\demarrer-tout.bat
```

**📋 Ce que fait le script :**

1. ✅ **Vérifie les prérequis** :
   - Docker (pour PostgreSQL)
   - Java (pour le backend)
   - Maven (pour le backend)
   - Node.js (pour le frontend)

2. ✅ **Lance PostgreSQL** dans Docker (fenêtre séparée)

3. ✅ **Lance le Backend Spring Boot** (fenêtre séparée)
   - URL : http://localhost:8080
   - Compile et démarre automatiquement

4. ✅ **Lance le Frontend Angular** (fenêtre séparée)
   - URL : http://localhost:4200
   - Installe les dépendances si nécessaire

**🎯 Résultat :**

Trois fenêtres de terminal s'ouvriront :
- **Fenêtre 1** : PostgreSQL (Docker)
- **Fenêtre 2** : Backend Spring Boot (http://localhost:8080)
- **Fenêtre 3** : Frontend Angular (http://localhost:4200)

**⏱️ Temps d'attente :**
- PostgreSQL : ~4 secondes
- Backend : ~8 secondes après PostgreSQL
- Frontend : démarre immédiatement après le backend

**🛑 Pour arrêter :**
Fermez les trois fenêtres de terminal qui se sont ouvertes.

---

## Méthode Manuelle (2 Terminaux)

### Étape 1 : Lancer le Backend

**Option A : Script automatique (Recommandé)**
```bash
demarrer-backend.bat
```

**Option B : Commandes manuelles**
```bash
cd backend
mvn spring-boot:run
```

**Attendez** le message : `Started PadelManagementApplication in X.XXX seconds`

### Étape 2 : Lancer le Frontend (dans un NOUVEAU terminal)

**Option A : Script automatique (Recommandé)**
```bash
demarrer-frontend.bat
```

**Option B : Commandes manuelles**
```bash
cd frontend
npm start
```

**L'application s'ouvrira automatiquement** sur http://localhost:4200

---

## ⚠️ Avant de Lancer

### Vérifier les Prérequis

Le script `demarrer-tout.bat` vérifie automatiquement tous les prérequis. Si un outil manque, il vous indiquera lequel installer.

**Vérification manuelle :**

1. **Java et Maven** (pour le backend)
   ```powershell
   java -version
   mvn -version
   ```
   
   **Si ces commandes ne fonctionnent pas :**
   - Utilisez le script d'installation : `installer-maven.ps1` (en tant qu'administrateur)
   - Ou consultez : `INSTALLER_MAVEN.md`
   - **Important :** Après installation, redémarrez votre terminal

2. **Node.js** (pour le frontend)
   ```powershell
   node --version
   npm --version
   ```
   
   **Si Node.js n'est pas installé :**
   - Téléchargez depuis : https://nodejs.org/
   - Installez la version LTS (Long Term Support)
   - **Important :** Après installation, redémarrez votre terminal

3. **Docker Desktop** (pour PostgreSQL)
   ```powershell
   docker --version
   docker compose version
   ```
   
   **Si Docker n'est pas installé :**
   - Téléchargez Docker Desktop : https://www.docker.com/products/docker-desktop/
   - Installez et redémarrez votre ordinateur
   - Assurez-vous que Docker Desktop est démarré avant de lancer le script

**⚠️ Important :**
- Après avoir installé un outil, **fermez et rouvrez votre terminal** (ou PowerShell)
- Les outils doivent être dans le PATH système pour être détectés

---

## 📍 URLs Importantes

Une fois lancé :

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:4200 | Interface utilisateur |
| **Backend API** | http://localhost:8080/api | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html | Documentation interactive |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | Spécification OpenAPI |
| **Base PostgreSQL** | localhost:5432 | DB: `padelService` (docker-compose) |

**Identifiants PostgreSQL :**
- DB : `padelService`
- User : `padel`
- Password : `padel`

**Script SQL (optionnel) :**
- `db/init.sql` (monté automatiquement dans PostgreSQL via docker-compose au 1er démarrage du volume)

---

## 🛑 Arrêter l'Application

- **Backend** : Appuyez sur `Ctrl+C` dans le terminal du backend
- **Frontend** : Appuyez sur `Ctrl+C` dans le terminal du frontend
- **Si lancé avec `demarrer-tout.bat`** : Fermez les fenêtres de terminal

---

## ❌ Problèmes Courants

### Le script `demarrer-tout.bat` s'arrête après "Appuyez sur une touche pour continuer..."

**Problème :** Le script détecte qu'un ou plusieurs outils sont manquants (Docker, Java, Maven, ou Node.js).

**Solution étape par étape :**

1. **Étape 1 : Relancez le script et lisez les messages d'erreur**
   ```powershell
   .\demarrer-tout.bat
   ```
   
   Le script affichera maintenant clairement :
   - `[OK]` pour les outils installés
   - `[X]` pour les outils manquants

2. **Étape 2 : Installez les outils manquants**
   
   Le script vous indiquera exactement quels outils installer et comment.

3. **Étape 3 : Redémarrez votre terminal**
   
   ⚠️ **Important :** Après avoir installé un outil, vous DEVEZ :
   - Fermer complètement votre terminal/PowerShell
   - Rouvrir un nouveau terminal
   - Relancer le script

4. **Étape 4 : Vérifiez manuellement (optionnel)**
   ```powershell
   # Vérifier Docker
   docker --version
   
   # Vérifier Java
   java -version
   
   # Vérifier Maven
   mvn -version
   
   # Vérifier Node.js
   node --version
   ```

**Si tous les outils sont installés mais le script ne les détecte pas :**
- Vérifiez que les outils sont dans le PATH système
- Redémarrez votre ordinateur après installation
- Utilisez un nouveau terminal (pas celui qui était ouvert pendant l'installation)

### "Maven n'est pas reconnu"
→ Installez Maven : `installer-maven.ps1` (en tant qu'administrateur)
→ Consultez : `INSTALLER_MAVEN.md`

### "Port déjà utilisé"
- Backend (8080) : Arrêtez l'application qui utilise le port 8080
- Frontend (4200) : Utilisez `ng serve --port 4201`

### "Node.js n'est pas reconnu"
→ Installez Node.js depuis https://nodejs.org/

### Le backend ne démarre pas
→ Vérifiez que Java est installé : `java -version`

### ❌ Erreur : "This command is not available when running the Angular CLI outside a workspace"

**Problème :** Vous avez essayé d'exécuter `ng serve` depuis le mauvais dossier (probablement `backend` au lieu de `frontend`).

**Solution étape par étape :**

1. **Étape 1 : Vérifiez votre position actuelle**
   ```powershell
   pwd
   # ou sur Windows CMD
   cd
   ```

2. **Étape 2 : Retournez à la racine du projet**
   ```powershell
   cd C:\Users\user\Desktop\padelService_projet_2
   ```

3. **Étape 3 : Allez dans le dossier frontend (pas backend !)**
   ```powershell
   cd frontend
   ```

4. **Étape 4 : Vérifiez que vous êtes au bon endroit**
   ```powershell
   # Vous devriez voir les fichiers : angular.json, package.json, tsconfig.json
   dir
   ```

5. **Étape 5 : Installez les dépendances si nécessaire**
   ```powershell
   npm install
   ```

6. **Étape 6 : Lancez Angular**
   ```powershell
   npm start
   # ou
   ng serve
   ```

**Solution rapide (recommandée) :**

Utilisez simplement le script fourni depuis la racine du projet :
```powershell
.\demarrer-frontend.bat
```

**Résumé :**
- ❌ `cd backend` puis `ng serve` → **ERREUR**
- ✅ `cd frontend` puis `ng serve` ou `npm start` → **OK**
- ✅ `.\demarrer-frontend.bat` depuis la racine → **OK**

### ❌ Erreur : "Fix the upstream dependency conflict" lors de `npm install`

**Problème :** Conflit de dépendances npm (erreur ERESOLVE) lors de l'installation des packages.

**Solution étape par étape :**

1. **Étape 1 : Allez dans le dossier frontend**
   ```powershell
   cd frontend
   ```

2. **Étape 2 : Supprimez node_modules et package-lock.json si ils existent**
   ```powershell
   # Supprimer node_modules
   Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
   
   # Supprimer package-lock.json
   Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
   ```

3. **Étape 3 : Utilisez --legacy-peer-deps pour installer les dépendances**
   ```powershell
   npm install --legacy-peer-deps
   ```
   
   Cette option permet à npm d'ignorer les conflits de peer dependencies.

4. **Étape 4 : Vérifiez que l'installation a réussi**
   ```powershell
   # Vous devriez voir un dossier node_modules créé
   dir
   ```

5. **Étape 5 : Lancez l'application**
   ```powershell
   npm start
   ```

**Solution alternative (si --legacy-peer-deps ne fonctionne pas) :**

```powershell
npm install --force
```

⚠️ **Note :** `--force` peut installer des versions incompatibles. Utilisez `--legacy-peer-deps` en priorité.

**Pour éviter ce problème à l'avenir :**

Créez ou modifiez le fichier `.npmrc` dans le dossier `frontend` :
```powershell
# Dans le dossier frontend
echo "legacy-peer-deps=true" > .npmrc
```

Ensuite, vous pourrez utiliser simplement :
```powershell
npm install
```

**Solution rapide (une seule commande) :**
```powershell
cd frontend
Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
npm install --legacy-peer-deps
npm start
```

### ❌ Erreur : "The Angular Compiler requires TypeScript >=5.4.0 and <5.6.0 but 5.6.3 was found"

**Problème :** Version de TypeScript incompatible avec Angular 18. Angular 18 nécessite TypeScript >=5.4.0 et <5.6.0, mais une version supérieure (5.6.3) a été installée.

**Solution étape par étape :**

1. **Étape 1 : Allez dans le dossier frontend**
   ```powershell
   cd frontend
   ```

2. **Étape 2 : Supprimez node_modules et package-lock.json**
   ```powershell
   Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
   Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
   ```

3. **Étape 3 : Vérifiez que package.json utilise la bonne version de TypeScript**
   
   Le fichier `package.json` doit contenir :
   ```json
   "typescript": "~5.5.0"
   ```
   
   ⚠️ **Si vous voyez `"typescript": "~5.6.0"`, il faut le corriger manuellement ou réinstaller.**

4. **Étape 4 : Réinstallez les dépendances avec la bonne version**
   ```powershell
   npm install --legacy-peer-deps
   ```

5. **Étape 5 : Vérifiez la version de TypeScript installée**
   ```powershell
   npx tsc --version
   ```
   
   Vous devriez voir une version entre 5.4.0 et 5.5.x (par exemple : 5.5.4)

6. **Étape 6 : Lancez l'application**
   ```powershell
   npm start
   ```

**Solution rapide (toutes les commandes en une fois) :**
```powershell
cd frontend
Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
npm install --legacy-peer-deps
npx tsc --version
npm start
```

**Note :** Le fichier `package.json` a été corrigé pour utiliser `"typescript": "~5.5.0"` qui est compatible avec Angular 18.

---

## ✅ Vérifier que Tout Fonctionne

1. **Backend** : http://localhost:8080/api/sites
   - Devrait retourner une liste JSON des sites

2. **Frontend** : http://localhost:4200
   - L'interface devrait s'afficher

3. **Base de données** : via Docker (postgres)
   - Les données d'exemple sont insérées automatiquement au démarrage.

---

## 🔐 Auth admin (HTTP Basic)

Les membres n'ont pas de login (ils utilisent leur matricule).

Identifiants d'exemple (seed) :
- Admin global : `AG0001` / `admin123`
- Admin site (Paris) : `AS0001` / `admin123`

Endpoints protégés :
- `GET /api/statistiques/*`
- Les opérations CRUD sur `/api/sites`, `/api/terrains`, `/api/membres` (POST/PUT/DELETE)

---

## 📚 Documentation Complète

Pour plus de détails, consultez :
- **[LANCEMENT.md](LANCEMENT.md)** - Guide complet de lancement
- **[INSTALLATION.md](INSTALLATION.md)** - Installation des dépendances
- **[EXEMPLES_DONNEES.md](EXEMPLES_DONNEES.md)** - Données d'exemple créées

---

**🎉 C'est tout ! L'application devrait maintenant fonctionner !**

