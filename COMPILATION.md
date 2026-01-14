# 📦 Guide de Compilation du Projet PadelService

## ✅ Corrections Effectuées

- ✅ Version Spring Boot corrigée : `3.5.9` → `3.3.0`
- ✅ Version Angular corrigée : `21.0.0` → `18.0.0`
- ✅ Tous les fichiers vérifiés et validés

## 🚀 Commandes de Compilation

### **Option 1 : Compilation Complète (Recommandée)**

#### Étape 1 : Démarrer la Base de Données PostgreSQL

```powershell
cd C:\Users\user\Desktop\padelService_projet_2
docker compose up -d
```

**OU** utiliser le script :
```cmd
demarrer-db.bat
```

#### Étape 2 : Compiler et Lancer le Backend Spring Boot

```powershell
cd C:\Users\user\Desktop\padelService_projet_2\backend
mvn clean install
mvn spring-boot:run
```

**OU** avec les variables d'environnement pour Docker :
```powershell
cd C:\Users\user\Desktop\padelService_projet_2\backend
$env:SPRING_PROFILES_ACTIVE="docker"
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="padelService"
$env:DB_USER="padel"
$env:DB_PASSWORD="padel"
mvn clean install
mvn spring-boot:run
```

**OU** utiliser le script :
```cmd
cd C:\Users\user\Desktop\padelService_projet_2
demarrer-backend.bat
```

#### Étape 3 : Installer les Dépendances et Lancer le Frontend Angular

Dans un **nouveau terminal** :

```powershell
cd C:\Users\user\Desktop\padelService_projet_2\frontend
npm install
npm start
```

**OU** utiliser le script :
```cmd
cd C:\Users\user\Desktop\padelService_projet_2
demarrer-frontend.bat
```

---

### **Option 2 : Lancer Tout en Une Fois**

```cmd
cd C:\Users\user\Desktop\padelService_projet_2
demarrer-tout.bat
```

Ce script lance automatiquement :
1. PostgreSQL (Docker)
2. Backend Spring Boot (compilation + démarrage)
3. Frontend Angular (installation npm + démarrage)

---

## 📋 Commandes Détaillées

### **1. Compilation Backend (Maven)**

```powershell
cd C:\Users\user\Desktop\padelService_projet_2\backend

# Nettoyer et compiler
mvn clean install

# Lancer l'application (avec profil docker)
$env:SPRING_PROFILES_ACTIVE="docker"
mvn spring-boot:run

# OU sans profil (utilise H2 en mémoire)
mvn spring-boot:run
```

**Sortie attendue :**
- Fichier JAR : `backend/target/padel-management-1.0.0.jar`
- Serveur démarré sur : http://localhost:8080

### **2. Compilation Frontend (Angular)**

```powershell
cd C:\Users\user\Desktop\padelService_projet_2\frontend

# Installer les dépendances (première fois seulement)
npm install

# Compiler pour la production
npm run build

# OU lancer en mode développement
npm start
```

**Sortie attendue :**
- Application compilée : `frontend/dist/padel-management/`
- Serveur de développement : http://localhost:4200

### **3. Base de Données PostgreSQL**

```powershell
cd C:\Users\user\Desktop\padelService_projet_2

# Démarrer PostgreSQL
docker compose up -d

# Vérifier que le conteneur tourne
docker ps

# Voir les logs
docker compose logs -f db

# Arrêter PostgreSQL
docker compose down
```

---

## 🔧 Vérifications Préalables

### Vérifier Java et Maven

```powershell
java -version
# Doit afficher Java 21 ou supérieur

mvn -version
# Doit afficher Maven 3.x
```

### Vérifier Node.js et npm

```powershell
node --version
# Doit afficher Node.js 18.x ou supérieur

npm --version
# Doit afficher npm 9.x ou supérieur
```

### Vérifier Docker

```powershell
docker --version
docker compose version
```

---

## 🎯 Commandes Rapides (Résumé)

```powershell
# 1. Démarrer la base de données
cd C:\Users\user\Desktop\padelService_projet_2
docker compose up -d

# 2. Compiler et lancer le backend
cd backend
mvn clean install
mvn spring-boot:run

# 3. Dans un nouveau terminal : installer et lancer le frontend
cd C:\Users\user\Desktop\padelService_projet_2\frontend
npm install
npm start
```

**OU** simplement :
```cmd
cd C:\Users\user\Desktop\padelService_projet_2
demarrer-tout.bat
```

---

## 📍 URLs Après Compilation

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:4200 | Interface utilisateur |
| **Backend API** | http://localhost:8080/api | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html | Documentation interactive |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs | Spécification OpenAPI |
| **PostgreSQL** | localhost:5432 | Base de données |

---

## 🛑 Arrêter les Services

```powershell
# Arrêter le backend : Ctrl+C dans le terminal du backend
# Arrêter le frontend : Ctrl+C dans le terminal du frontend
# Arrêter PostgreSQL
cd C:\Users\user\Desktop\padelService_projet_2
docker compose down
```

---

## ✅ Vérification Post-Compilation

1. **Backend** : http://localhost:8080/api/sites
   - Devrait retourner une liste JSON des sites

2. **Frontend** : http://localhost:4200
   - L'interface devrait s'afficher correctement

3. **Base de données** :
   ```powershell
   docker exec -it padelservice-db psql -U padel -d padelService
   # Dans psql:
   SELECT * FROM sites;
   \q
   ```

---

## 🐛 Dépannage

### Erreur "Port déjà utilisé"
```powershell
# Windows : trouver le processus qui utilise le port
netstat -ano | findstr :8080
netstat -ano | findstr :4200
netstat -ano | findstr :5432

# Tuer le processus (remplacer PID par le numéro trouvé)
taskkill /PID <PID> /F
```

### Erreur "Maven n'est pas reconnu"
→ Installer Maven ou ajouter Maven au PATH

### Erreur "Node.js n'est pas reconnu"
→ Installer Node.js depuis https://nodejs.org/

### Erreur "Docker n'est pas démarré"
→ Démarrer Docker Desktop

---

**🎉 Le projet est maintenant prêt à être compilé et lancé !**


