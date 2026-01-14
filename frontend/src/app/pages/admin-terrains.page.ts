import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdminAuthService } from '../services/admin-auth.service';
import { PadelApiService } from '../services/padel-api.service';
import { Site, Terrain } from '../services/api';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="row" style="justify-content:space-between; align-items:center; gap:12px">
      <h2 style="margin:0">Administration • Terrains</h2>
      <div class="row" style="gap:8px">
        <a class="btn btn-secondary" routerLink="/admin/dashboard">Dashboard</a>
        <a class="btn btn-secondary" routerLink="/admin/sites">Sites</a>
        <a class="btn btn-secondary" routerLink="/admin/membres">Membres</a>
        <button class="btn" (click)="logout()">Déconnexion</button>
      </div>
    </div>

    <div class="card" *ngIf="!auth.isLoggedIn()">
      <div class="error">Vous devez vous authentifier d'abord.</div>
      <a class="btn" routerLink="/admin">Aller au login</a>
    </div>

    <div class="grid grid-2" *ngIf="auth.isLoggedIn()">
      <div class="card">
        <h3>Créer un terrain</h3>

        <label>Nom</label>
        <input [(ngModel)]="createNom" placeholder="ex: Terrain A" />

        <label style="margin-top:8px">Site</label>
        <select [(ngModel)]="createSiteId" style="margin-top:6px">
          <option [ngValue]="null">-- Choisir --</option>
          <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
        </select>

        <button class="btn" style="margin-top:12px" (click)="createTerrain()">Créer</button>
        <div class="hint" style="margin-top:10px" *ngIf="message">{{ message }}</div>
      </div>

      <div class="card">
        <div class="row" style="justify-content:space-between; align-items:center">
          <h3 style="margin:0">Liste des terrains</h3>
          <button class="btn btn-secondary" (click)="refresh()">Rafraîchir</button>
        </div>

        <div class="row" style="gap:8px; margin-top:10px; align-items:center">
          <label style="margin:0">Filtrer par site</label>
          <select [(ngModel)]="filterSiteId" (change)="applyFilter()">
            <option [ngValue]="null">Tous</option>
            <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
          </select>
        </div>

        <table style="width:100%; margin-top:10px">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom</th>
              <th>Site</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let t of filteredTerrains">
              <td>{{ t.id }}</td>
              <td><input [(ngModel)]="t.nom" /></td>
              <td style="min-width:180px">
                <select [ngModel]="getSiteId(t)" (ngModelChange)="setSiteId(t, $event)">
                  <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
                </select>
              </td>
              <td style="white-space:nowrap">
                <button class="btn btn-secondary" (click)="saveTerrain(t)">Enregistrer</button>
                <button class="btn" style="margin-left:6px" (click)="deleteTerrain(t)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="hint" style="margin-top:10px">
          Astuce : un terrain appartient obligatoirement à un site.
        </div>
      </div>
    </div>
  `
})
export class AdminTerrainsPageComponent {
  sites: Site[] = [];
  terrains: Terrain[] = [];
  filteredTerrains: any[] = [];

  createNom = '';
  createSiteId: number | null = null;

  filterSiteId: number | null = null;

  message = '';

  constructor(
    public auth: AdminAuthService,
    private api: PadelApiService,
    private router: Router
  ) {
    if (!this.auth.isLoggedIn()) return;
    this.loadSitesThenTerrains();
  }

  private loadSitesThenTerrains(): void {
    this.api.getSites().subscribe({
      next: s => {
        this.sites = s as any[];
        this.refresh();
      },
      error: () => (this.message = 'Erreur: backend non démarré ou accès refusé.')
    });
  }

  refresh(): void {
    this.api.getTerrains().subscribe({
      next: ts => {
        this.terrains = (ts as any[]).map(t => ({
          ...t,
          siteId: (t as any)?.site?.id ?? (t as any)?.siteId ?? null
        }));
        this.applyFilter();
      },
      error: () => (this.message = 'Erreur: backend non démarré ou accès refusé.')
    });
  }

  applyFilter(): void {
    if (!this.filterSiteId) {
      this.filteredTerrains = [...(this.terrains as any[])];
      return;
    }
    this.filteredTerrains = (this.terrains as any[]).filter(t => (t as any).siteId === this.filterSiteId);
  }

  getSiteId(t: any): number | null {
    return (t as any)?.siteId ?? (t as any)?.site?.id ?? null;
  }

  setSiteId(t: any, value: number | null): void {
    (t as any).siteId = value;
  }

  createTerrain(): void {
    this.message = '';
    const nom = this.createNom?.trim();
    if (!nom) {
      this.message = 'Le nom est obligatoire.';
      return;
    }
    if (!this.createSiteId) {
      this.message = 'Le site est obligatoire.';
      return;
    }
    this.api.createTerrain({ nom, siteId: this.createSiteId }).subscribe({
      next: () => {
        this.message = 'Terrain créé.';
        this.createNom = '';
        this.refresh();
      },
      error: () => (this.message = 'Création impossible (droits admin requis).')
    });
  }

  saveTerrain(t: any): void {
    if (!t.id) return;
    this.api.updateTerrain(t.id, { nom: t.nom, siteId: t.siteId }).subscribe({
      next: () => (this.message = 'Terrain mis à jour.'),
      error: () => (this.message = 'Mise à jour impossible (droits admin requis).')
    });
  }

  deleteTerrain(t: any): void {
    if (!t.id) return;
    if (!confirm(`Supprimer le terrain "${t.nom}" ?`)) return;
    this.api.deleteTerrain(t.id).subscribe({
      next: () => {
        this.message = 'Terrain supprimé.';
        this.refresh();
      },
      error: () => (this.message = 'Suppression impossible (droits admin requis).')
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigateByUrl('/admin');
  }
}
