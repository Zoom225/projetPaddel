# Guide de lancement (Windows)

## 1) Prérequis

- **Java 21**
- **Maven 3.9+**
- **Node.js 20+**
- **Docker Desktop** (pour PostgreSQL)

Vérifier :
```bash
java -version
mvn -version
node -version
docker --version
docker compose version
```

## 2) Base de données PostgreSQL (DB = padelService)

Depuis la racine du projet :
```bash
docker compose up -d
```

Paramètres :
- Host : `localhost`
- Port : `5432`
- DB : `padelService`
- User : `padel`
- Password : `padel`

## 3) Lancer le backend (Spring Boot)

Option script (Windows) :
```bash
demarrer-backend.bat
```

Option manuelle :
```bash
cd backend
set SPRING_PROFILES_ACTIVE=docker
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=padelService
set DB_USER=padel
set DB_PASSWORD=padel
mvn spring-boot:run
```

Backend : http://localhost:8080

## 4) Lancer le frontend (Angular)

```bash
cd frontend
npm install
npm start
```

Frontend : http://localhost:4200

## 5) Vérifications rapides

- Sites : http://localhost:8080/api/sites
- Matches publics : http://localhost:8080/api/matches/public

## 6) Comptes et rôles

Les **membres** n'ont pas de login (ils utilisent leur matricule pour créer/payer des matches).

Les **administrateurs** utilisent une authentification **HTTP Basic** (pour les statistiques + CRUD).

Comptes seed (créés au démarrage, voir `DataInitializer.java`) :
- Admin global : **AG0001** / **admin123**
- Admin site (Paris) : **AS0001** / **admin123**

Endpoint test :
```bash
curl -u AG0001:admin123 http://localhost:8080/api/statistiques/globales
```
