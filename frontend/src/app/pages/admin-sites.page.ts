import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdminAuthService } from '../services/admin-auth.service';
import { PadelApiService } from '../services/padel-api.service';
import { Site } from '../services/api';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="row" style="justify-content:space-between; align-items:center; gap:12px">
      <h2 style="margin:0">Administration • Sites</h2>
      <div class="row" style="gap:8px">
        <a class="btn btn-secondary" routerLink="/admin/dashboard">Dashboard</a>
        <a class="btn btn-secondary" routerLink="/admin/terrains">Terrains</a>
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
        <h3>Créer un site</h3>

        <label>Nom</label>
        <input [(ngModel)]="createNom" placeholder="ex: Bruxelles" />

        <div class="grid grid-2" style="margin-top:8px">
          <div>
            <label>Heure début</label>
            <input [(ngModel)]="createHeureDebut" placeholder="08:00" />
          </div>
          <div>
            <label>Heure fin</label>
            <input [(ngModel)]="createHeureFin" placeholder="22:00" />
          </div>
        </div>

        <div class="grid grid-2" style="margin-top:8px">
          <div>
            <label>Durée match (min)</label>
            <input type="number" [(ngModel)]="createDureeMatch" />
          </div>
          <div>
            <label>Pause entre matchs (min)</label>
            <input type="number" [(ngModel)]="createDureeEntre" />
          </div>
        </div>

        <button class="btn" style="margin-top:12px" (click)="createSite()">Créer</button>
        <div class="hint" style="margin-top:10px" *ngIf="message">{{ message }}</div>
      </div>

      <div class="card">
        <div class="row" style="justify-content:space-between; align-items:center">
          <h3 style="margin:0">Liste des sites</h3>
          <button class="btn btn-secondary" (click)="refresh()">Rafraîchir</button>
        </div>

        <table style="width:100%; margin-top:10px">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom</th>
              <th>Heures</th>
              <th>Match</th>
              <th>Pause</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let s of sites">
              <td>{{ s.id }}</td>
              <td>
                <input [(ngModel)]="s.nom" />
              </td>
              <td style="min-width:180px">
                <div class="row" style="gap:6px">
                  <input style="width:80px" [ngModel]="getHeureDebut(s)" (ngModelChange)="setHeureDebut(s, $event)" placeholder="08:00" />
                  <span>→</span>
                  <input style="width:80px" [ngModel]="getHeureFin(s)" (ngModelChange)="setHeureFin(s, $event)" placeholder="22:00" />
                </div>
              </td>
              <td style="width:110px">
                <input type="number" [ngModel]="getDureeMatchMinutes(s)" (ngModelChange)="setDureeMatchMinutes(s, $event)" />
              </td>
              <td style="width:110px">
                <input type="number" [ngModel]="getDureeEntreMatchMinutes(s)" (ngModelChange)="setDureeEntreMatchMinutes(s, $event)" />
              </td>
              <td style="white-space:nowrap">
                <button class="btn btn-secondary" (click)="saveSite(s)">Enregistrer</button>
                <button class="btn" style="margin-left:6px" (click)="deleteSite(s)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="hint" style="margin-top:10px">
          Swagger UI : <code>http://localhost:8080/swagger-ui/index.html</code>
        </div>
      </div>
    </div>
  `
})
export class AdminSitesPageComponent {
  sites: Site[] = [];

  createNom = '';
  createHeureDebut = '08:00';
  createHeureFin = '22:00';
  createDureeMatch = 90;
  createDureeEntre = 15;

  message = '';

  constructor(
    public auth: AdminAuthService,
    private api: PadelApiService,
    private router: Router
  ) {
    if (!this.auth.isLoggedIn()) {
      return;
    }
    this.refresh();
  }

  refresh(): void {
    this.api.getSites().subscribe({
      next: s => {
        this.sites = (s as any[]).map(site => ({
          ...site,
          heureDebut: site.heureDebut || '',
          heureFin: site.heureFin || '',
          dureeMatchMinutes: site.dureeMatchMinutes || 90,
          dureeEntreMatchMinutes: site.dureeEntreMatchMinutes || 15
        }));
      },
      error: () => (this.message = 'Erreur: backend non démarré ou accès refusé.')
    });
  }

  getHeureDebut(s: any): string {
    return s.heureDebut || '';
  }

  setHeureDebut(s: any, value: string): void {
    s.heureDebut = value;
  }

  getHeureFin(s: any): string {
    return s.heureFin || '';
  }

  setHeureFin(s: any, value: string): void {
    s.heureFin = value;
  }

  getDureeMatchMinutes(s: any): number {
    return s.dureeMatchMinutes || 90;
  }

  setDureeMatchMinutes(s: any, value: number): void {
    s.dureeMatchMinutes = value;
  }

  getDureeEntreMatchMinutes(s: any): number {
    return s.dureeEntreMatchMinutes || 15;
  }

  setDureeEntreMatchMinutes(s: any, value: number): void {
    s.dureeEntreMatchMinutes = value;
  }

  createSite(): void {
    this.message = '';
    const payload: any = {
      nom: this.createNom?.trim(),
      heureDebut: this.createHeureDebut,
      heureFin: this.createHeureFin,
      dureeMatchMinutes: this.createDureeMatch,
      dureeEntreMatchMinutes: this.createDureeEntre
    };

    if (!payload.nom) {
      this.message = 'Le nom est obligatoire.';
      return;
    }

    this.api.createSite(payload).subscribe({
      next: () => {
        this.message = 'Site créé.';
        this.createNom = '';
        this.refresh();
      },
      error: () => (this.message = 'Création impossible (droits admin requis).')
    });
  }

  saveSite(site: Site): void {
    if (!site.id) return;
    const payload: any = {
      nom: site.nom,
      heureDebut: (site as any).heureDebut,
      heureFin: (site as any).heureFin,
      dureeMatchMinutes: (site as any).dureeMatchMinutes,
      dureeEntreMatchMinutes: (site as any).dureeEntreMatchMinutes
    };
    this.api.updateSite(site.id, payload).subscribe({
      next: () => (this.message = 'Site mis à jour.'),
      error: () => (this.message = 'Mise à jour impossible (droits admin requis).')
    });
  }

  deleteSite(site: Site): void {
    if (!site.id) return;
    if (!confirm(`Supprimer le site "${site.nom}" ?`)) return;
    this.api.deleteSite(site.id).subscribe({
      next: () => {
        this.message = 'Site supprimé.';
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
