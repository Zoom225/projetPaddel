import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PadelApiService } from '../services/padel-api.service';
import { Match, Site, Terrain } from '../services/api';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8 px-4">
      <div class="max-w-7xl mx-auto">
        <!-- Header -->
        <div class="mb-8">
          <h1 class="text-4xl font-bold text-gray-800 mb-2">🎾 Mes Réservations</h1>
          <p class="text-gray-600">Gérez vos matchs de padel et vos paiements</p>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <!-- LEFT SECTION: Voir mes matches -->
          <div class="lg:col-span-2">
            <div class="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
              <div class="bg-gradient-to-r from-blue-500 to-blue-600 px-8 py-6">
                <h2 class="text-2xl font-bold text-white flex items-center gap-2">
                  <span>📋</span> Voir mes matchs
                </h2>
                <p class="text-blue-100 mt-1 text-sm">Consultez tous vos matchs réservés</p>
              </div>

              <div class="p-8">
                <!-- Search Input -->
                <div class="mb-6">
                  <label class="block text-sm font-semibold text-gray-700 mb-2">Matricule</label>
                  <div class="flex gap-3">
                    <input 
                      [(ngModel)]="matricule" 
                      placeholder="Ex: AG0001, AS00001, L00001"
                      class="flex-1 px-4 py-3 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-blue-500 transition-colors"
                    />
                    <button 
                      (click)="loadMyMatches()"
                      class="px-6 py-3 bg-blue-500 hover:bg-blue-600 text-white font-semibold rounded-lg transition-colors shadow-md"
                    >
                      🔍 Chercher
                    </button>
                  </div>
                </div>

                <!-- Error Message -->
                <div *ngIf="error" class="mb-6 p-4 bg-red-50 border-l-4 border-red-500 text-red-700 rounded">
                  <p class="font-semibold">⚠️ Erreur</p>
                  <p class="text-sm mt-1">{{ error }}</p>
                </div>

                <!-- Matches List -->
                <div *ngIf="myMatches.length === 0 && !error" class="text-center py-12 text-gray-500">
                  <p class="text-lg">📭 Aucun match trouvé</p>
                  <p class="text-sm mt-1">Entrez votre matricule pour voir vos matchs</p>
                </div>

                <div *ngIf="myMatches.length > 0" class="space-y-4">
                  <div class="text-sm font-semibold text-gray-600 mb-4">{{ myMatches.length }} match(s) trouvé(s)</div>
                  
                  <div *ngFor="let m of myMatches; let i = index" 
                       class="border-2 border-gray-200 rounded-lg p-6 hover:border-blue-400 hover:shadow-md transition-all"
                       [class.bg-blue-50]="m.estPaye"
                       [class.bg-gray-50]="!m.estPaye">
                    
                    <div class="flex justify-between items-start gap-4">
                      <div class="flex-1">
                        <h3 class="font-bold text-lg text-gray-800">
                          📍 {{ m.terrain?.site?.nom }} - {{ m.terrain?.nom }}
                        </h3>
                        
                        <div class="mt-3 grid grid-cols-2 gap-4 text-sm text-gray-600">
                          <div class="flex items-center gap-2">
                            <span class="text-lg">📅</span>
                            <span>{{ m.date }}</span>
                          </div>
                          <div class="flex items-center gap-2">
                            <span class="text-lg">⏰</span>
                            <span>{{ m.heureDebut }} - {{ m.heureFin }}</span>
                          </div>
                          <div class="flex items-center gap-2">
                            <span class="text-lg">👥</span>
                            <span>{{ (m.joueurs||[]).length }} / 4 joueurs</span>
                          </div>
                          <div class="flex items-center gap-2">
                            <span class="text-lg">🏷️</span>
                            <span [class.text-green-600]="m.typeMatch === 'PUBLIC'" 
                                  [class.text-purple-600]="m.typeMatch === 'PRIVE'">
                              {{ m.typeMatch }}
                            </span>
                          </div>
                        </div>
                      </div>

                      <div class="flex flex-col gap-2">
                        <button 
                          (click)="payer(m.id)"
                          [disabled]="m.estPaye"
                          [class.opacity-50]="m.estPaye"
                          [class.cursor-not-allowed]="m.estPaye"
                          class="px-4 py-2 rounded-lg font-semibold transition-all whitespace-nowrap"
                          [class.bg-green-500]="m.estPaye"
                          [class.text-white]="m.estPaye"
                          [class.bg-yellow-500]="!m.estPaye"
                          [class.text-white]="!m.estPaye"
                          [class.hover:bg-yellow-600]="!m.estPaye"
                        >
                          {{ m.estPaye ? '✅ Payé' : '💳 Payer' }}
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- RIGHT SECTION: Créer un match -->
          <div class="lg:col-span-1">
            <div class="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow sticky top-8">
              <div class="bg-gradient-to-r from-green-500 to-green-600 px-8 py-6">
                <h2 class="text-2xl font-bold text-white flex items-center gap-2">
                  <span>➕</span> Créer Match
                </h2>
                <p class="text-green-100 mt-1 text-sm">Organiser un nouveau match</p>
              </div>

              <div class="p-6">
                <!-- Matricule -->
                <div class="mb-4">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Matricule</label>
                  <input 
                    [(ngModel)]="create.matriculeOrganisateur" 
                    placeholder="AG0001"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  />
                </div>

                <!-- Site -->
                <div class="mb-4">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Site</label>
                  <select 
                    [(ngModel)]="selectedSiteId" 
                    (change)="loadTerrains()"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  >
                    <option [ngValue]="null">Choisir...</option>
                    <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
                  </select>
                </div>

                <!-- Terrain -->
                <div class="mb-4">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Terrain</label>
                  <select 
                    [(ngModel)]="create.terrainId"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  >
                    <option [ngValue]="null">Choisir...</option>
                    <option *ngFor="let t of terrains" [ngValue]="t.id">{{ t.nom }}</option>
                  </select>
                </div>

                <!-- Date -->
                <div class="mb-4">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Date</label>
                  <input 
                    type="date" 
                    [(ngModel)]="create.date"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  />
                </div>

                <!-- Heure -->
                <div class="mb-4">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Heure début</label>
                  <input 
                    type="time" 
                    [(ngModel)]="create.heureDebut"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  />
                </div>

                <!-- Type Match -->
                <div class="mb-6">
                  <label class="block text-xs font-bold text-gray-700 mb-2 uppercase">Type</label>
                  <select 
                    [(ngModel)]="create.typeMatch"
                    class="w-full px-3 py-2 border-2 border-gray-200 rounded-lg focus:outline-none focus:border-green-500 transition-colors text-sm"
                  >
                    <option value="PUBLIC">🌍 PUBLIC</option>
                    <option value="PRIVE">🔒 PRIVÉ</option>
                  </select>
                </div>

                <!-- Submit Button -->
                <button 
                  (click)="createMatch()"
                  class="w-full px-4 py-3 bg-green-500 hover:bg-green-600 text-white font-bold rounded-lg transition-all shadow-md"
                >
                  ➕ Créer Match
                </button>

                <!-- Messages -->
                <div *ngIf="createError" class="mt-4 p-3 bg-red-50 border-l-4 border-red-500 text-red-700 rounded text-sm">
                  <p class="font-semibold">⚠️ Erreur</p>
                  <p class="text-xs mt-1">{{ createError }}</p>
                </div>

                <div *ngIf="createOk" class="mt-4 p-3 bg-green-50 border-l-4 border-green-500 text-green-700 rounded text-sm">
                  <p class="font-semibold">✅ Succès</p>
                  <p class="text-xs mt-1">{{ createOk }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class MyReservationsPageComponent {
  matricule = '';
  error: string | null = null;
  myMatches: Match[] = [];

  sites: Site[] = [];
  terrains: Terrain[] = [];
  selectedSiteId: number | null = null;

  create = {
    matriculeOrganisateur: '',
    terrainId: null as number | null,
    date: '',
    heureDebut: '',
    typeMatch: 'PUBLIC' as 'PRIVE' | 'PUBLIC'
  };
  createError: string | null = null;
  createOk: string | null = null;

  constructor(private api: PadelApiService) {
    this.api.getSites().subscribe({ next: s => this.sites = s });
  }

  loadMyMatches(): void {
    this.error = null;
    this.myMatches = [];
    if (!this.matricule.trim()) {
      this.error = 'Veuillez entrer un matricule.';
      return;
    }
    this.api.getMatchesByMembre(this.matricule.trim()).subscribe({
      next: data => this.myMatches = data,
      error: () => this.error = "Erreur lors du chargement (matricule inconnu ? backend non démarré ?)"
    });
  }

  payer(matchId: number): void {
    if (!this.matricule.trim()) {
      this.error = 'Entrez d’abord votre matricule dans la zone de recherche.';
      return;
    }
    this.api.payer(matchId, this.matricule.trim()).subscribe({
      next: () => this.loadMyMatches(),
      error: () => this.error = "Paiement impossible (règles métier / backend)"
    });
  }

  loadTerrains(): void {
    this.terrains = [];
    this.create.terrainId = null;
    if (!this.selectedSiteId) return;
    this.api.getTerrainsBySite(this.selectedSiteId).subscribe({ next: t => this.terrains = t });
  }

  createMatch(): void {
    this.createError = null;
    this.createOk = null;

    if (!this.create.matriculeOrganisateur.trim() || !this.create.terrainId || !this.create.date || !this.create.heureDebut) {
      this.createError = 'Tous les champs sont obligatoires.';
      return;
    }

    this.api.createMatch({
      matriculeOrganisateur: this.create.matriculeOrganisateur.trim(),
      terrainId: this.create.terrainId,
      date: this.create.date,
      heureDebut: this.create.heureDebut,
      typeMatch: this.create.typeMatch
    }).subscribe({
      next: (m) => {
        this.createOk = `Match créé (id=${m.id}).`;
      },
      error: () => this.createError = "Création impossible (règles métier / paramètres invalides)"
    });
  }
}
