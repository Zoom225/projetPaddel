# 🎾 Frontend Padel Management System

## 📋 Structure du Frontend

```
frontend/
├── src/
│   ├── app/
│   │   ├── pages/                    # Pages principales
│   │   │   ├── admin-dashboard.page.ts          # Tableau de bord admin
│   │   │   ├── admin-login.page.ts              # Login admin
│   │   │   ├── admin-membres.page.ts            # Gestion des membres
│   │   │   ├── admin-sites.page.ts              # Gestion des sites
│   │   │   ├── admin-terrains.page.ts           # Gestion des terrains
│   │   │   ├── home.page.ts                     # Accueil public
│   │   │   ├── my-reservations.page.ts          # Mes réservations (utilisateur)
│   │   │   └── public-matches.page.ts           # Matchs publics
│   │   │
│   │   ├── services/                # Services API et authentification
│   │   │   ├── admin-auth.service.ts            # Service d'authentification admin
│   │   │   ├── admin-auth.interceptor.ts        # Intercepteur JWT
│   │   │   ├── api.service.ts                   # Service API générique
│   │   │   ├── padel-api.service.ts             # Service API spécifique Padel
│   │   │   └── api.ts                           # Configuration de base API
│   │   │
│   │   ├── app.component.ts          # Composant racine
│   │   ├── app.routes.ts             # Routing principal
│   │   └── app.module.ts             # Module principal
│   │
│   ├── assets/                       # Images, icônes, etc.
│   ├── styles.css                    # Styles globaux
│   ├── index.html                    # HTML principal
│   └── main.ts                       # Point d'entrée Angular
│
├── package.json                      # Dépendances npm
├── angular.json                      # Configuration Angular
├── tsconfig.json                     # Configuration TypeScript
└── README.md                         # Ce fichier
```

---

## 🚀 Démarrage du Frontend

### Prérequis
```powershell
node --version          # ✅ v20+ requis
npm --version           # ✅ 10+ requis
```

### Installation et lancement

```powershell
cd frontend

# 1. Installer les dépendances (une seule fois)
npm install

# 2. Démarrer le serveur de développement
ng serve --port 4200
# OU
npm start

# ✅ Attendez : "Application bundle generation complete"
# Ouvrez : http://localhost:4200
```

---

## 📋 Pages et Fonctionnalités

### 🔓 Pages Publiques

**Home (`/`)**
- Accueil du site
- Présentation générale
- Lien vers login admin

**Public Matches (`/matches`)**
- Liste des matchs publics
- Filtre par site, terrain, date
- Consultation des détails

### 🔐 Pages Admin (Authentifiées)

**Admin Login (`/admin/login`)**
- Formulaire de connexion
- Authentification JWT
- Redirection après succès

**Admin Dashboard (`/admin/dashboard`)**
- Vue d'ensemble des statistiques
- Graphiques et métriques
- Liens vers les gestions

**Gestion Membres (`/admin/membres`)**
- Liste de tous les membres
- Ajouter/modifier/supprimer membre
- Voir les réservations par membre

**Gestion Sites (`/admin/sites`)**
- Liste de tous les sites
- Créer/éditer/supprimer site
- Voir les terrains par site

**Gestion Terrains (`/admin/terrains`)**
- Liste de tous les terrains
- Ajouter/modifier/supprimer terrain
- Assigner à un site

**Mes Réservations (`/user/reservations`)**
- Liste de mes réservations
- Créer nouvelle réservation
- Annuler réservation

---

## 🔧 Architecture Services

### `api.service.ts`
Service générique pour les appels HTTP
- GET, POST, PUT, DELETE
- Gestion des erreurs

### `admin-auth.service.ts`
Authentification Admin
- Login/Logout
- Stockage JWT (localStorage)
- Vérification de session

### `admin-auth.interceptor.ts`
Intercepteur HTTP
- Ajoute le token JWT aux requêtes
- Gère les erreurs 401 (Unauthorized)
- Redirection vers login

### `padel-api.service.ts`
API métier Padel
- Appels aux endpoints backend
- Gestion des données métier
- Mise en cache

---

## 🎨 Styling

Le projet utilise :
- **Tailwind CSS** (configuration dans `tailwind.config.js`)
- **CSS Global** (`styles.css`)
- **CSS Inline** (dans les composants)

Pour ajouter du CSS global :
```css
/* styles.css */
.ma-classe {
  color: #333;
  padding: 10px;
}
```

---

## 🧪 Commandes Utiles

```powershell
# Lancer en développement
ng serve

# Compiler pour la production
ng build --configuration production

# Lancer les tests
ng test

# Linter le code
ng lint

# Voir les dépendances
npm list

# Mettre à jour les dépendances
npm update

# Nettoyer et réinstaller
npm cache clean --force
Remove-Item -Path node_modules -Recurse -Force
npm install
```

---

## 🔗 Communication avec le Backend

Tous les appels API vont vers **http://localhost:8080**

### Exemple d'appel API
```typescript
// Dans un service
constructor(private http: HttpClient) {}

getSites() {
  return this.http.get('/api/sites');
}

// Utilisation dans un composant
constructor(private padelApi: PadelApiService) {}

ngOnInit() {
  this.padelApi.getSites().subscribe(
    (sites) => console.log(sites),
    (error) => console.error(error)
  );
}
```

### JWT Authentication
```typescript
// Le token est automatiquement ajouté par l'intercepteur
// Stockage : localStorage.getItem('token')
// En-tête : Authorization: Bearer <token>
```

---

## 📝 Ajouter une Nouvelle Page

### 1. Créer le composant
```bash
ng generate component pages/ma-page
```

### 2. Ajouter la route
```typescript
// app.routes.ts
export const routes: Routes = [
  {
    path: 'ma-page',
    component: MaPageComponent,
    canActivate: [AdminAuthGuard]  // Si protégée
  }
];
```

### 3. Créer le service si nécessaire
```typescript
@Injectable({ providedIn: 'root' })
export class MonService {
  constructor(private http: HttpClient) {}
  
  getData() {
    return this.http.get('/api/mon-endpoint');
  }
}
```

### 4. Utiliser dans le composant
```typescript
export class MaPageComponent implements OnInit {
  constructor(private service: MonService) {}
  
  ngOnInit() {
    this.service.getData().subscribe(data => {
      console.log(data);
    });
  }
}
```

---

## 🐛 Dépannage Frontend

### Erreur : "Cannot find module '@angular/...'"
```powershell
npm install
# OU
npm cache clean --force
Remove-Item -Path node_modules -Recurse -Force
npm install
```

### Erreur : "Port 4200 already in use"
```powershell
ng serve --port 4201
```

### Logs lents / Compilation lente
```powershell
# Reconstruire
ng build --configuration development
# Ou
npm cache clean --force
```

### Le backend ne répond pas
- Vérifier que le backend tourne : http://localhost:8080
- Vérifier les CORS (devrait être OK en dev)
- Voir la console du navigateur (F12 → Console)

---

## 📚 Documentation

- [Angular Docs](https://angular.io/docs)
- [TypeScript Docs](https://www.typescriptlang.org/docs)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [API Backend Documentation](http://localhost:8080/swagger-ui/index.html)

---

## ✅ Checklist Frontend

- [ ] Node.js 20+ installé
- [ ] npm 10+ installé
- [ ] `npm install` exécuté
- [ ] Backend tourne sur port 8080
- [ ] `ng serve` lance sans erreur
- [ ] http://localhost:4200 s'affiche
- [ ] Console (F12) sans erreurs rouges
- [ ] Les données du backend s'affichent

---

**Profitez du développement ! 🎾**
