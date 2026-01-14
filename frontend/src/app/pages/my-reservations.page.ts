import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PadelApiService } from '../services/padel-api.service';
import { Match, Site, Terrain } from '../services/api';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Mes réservations (par matricule)</h2>

    <div class="grid grid-2">
      <div class="card">
        <h3>1) Voir mes matches</h3>
        <label>Matricule (ex: G0001, S00001, L00001)</label>
        <input [(ngModel)]="matricule" placeholder="Entrez votre matricule"/>
        <button class="btn" style="margin-top:12px" (click)="loadMyMatches()">Rechercher</button>
        <div *ngIf="error" class="error" style="margin-top:12px">{{ error }}</div>

        <div style="margin-top:16px" *ngIf="myMatches.length">
          <div class="card" style="margin-bottom:12px" *ngFor="let m of myMatches">
            <strong>{{ m.terrain?.site?.nom }}</strong> — {{ m.terrain?.nom }}
            <div style="color:#666">{{ m.date }} · {{ m.heureDebut }} - {{ m.heureFin }} · {{ m.typeMatch }}</div>
            <div style="margin-top:8px; display:flex; gap:10px; flex-wrap:wrap">
              <button class="btn secondary" (click)="payer(m.id)">Payer</button>
              <span>Joueurs: {{ (m.joueurs||[]).length }}/4</span>
              <span>Payé: {{ m.estPaye ? 'oui' : 'non' }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <h3>2) Créer un match</h3>
        <p style="color:#666; margin-top:-6px">Le backend appliquera les règles métier (délais selon le type de membre, privé/public, etc.).</p>

        <label>Matricule organisateur</label>
        <input [(ngModel)]="create.matriculeOrganisateur" placeholder="G0001"/>

        <label style="margin-top:10px">Site</label>
        <select [(ngModel)]="selectedSiteId" (change)="loadTerrains()">
          <option [ngValue]="null">-- Choisir --</option>
          <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
        </select>

        <label style="margin-top:10px">Terrain</label>
        <select [(ngModel)]="create.terrainId">
          <option [ngValue]="null">-- Choisir --</option>
          <option *ngFor="let t of terrains" [ngValue]="t.id">{{ t.nom }}</option>
        </select>

        <label style="margin-top:10px">Date</label>
        <input type="date" [(ngModel)]="create.date"/>

        <label style="margin-top:10px">Heure de début</label>
        <input type="time" [(ngModel)]="create.heureDebut"/>

        <label style="margin-top:10px">Type de match</label>
        <select [(ngModel)]="create.typeMatch">
          <option value="PUBLIC">PUBLIC</option>
          <option value="PRIVE">PRIVE</option>
        </select>

        <button class="btn" style="margin-top:12px" (click)="createMatch()">Créer</button>
        <div *ngIf="createError" class="error" style="margin-top:12px">{{ createError }}</div>
        <div *ngIf="createOk" style="margin-top:12px; color:#0a7a0a">{{ createOk }}</div>
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
