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
      <h2 style="margin:0">Dashboard admin</h2>
      <div class="row" style="gap:8px">
        <a class="btn btn-secondary" routerLink="/admin/sites">Sites</a>
        <a class="btn btn-secondary" routerLink="/admin/terrains">Terrains</a>
        <a class="btn btn-secondary" routerLink="/admin/membres">Membres</a>
        <button class="btn" (click)="logout()">Déconnexion</button>
      </div>
    </div>

    <div class="card" *ngIf="!auth.isLoggedIn()">
      <div class="error">Vous devez vous authentifier d'abord.</div>
    </div>

    <div class="grid grid-2" *ngIf="auth.isLoggedIn()">
      <div class="card">
        <h3>Statistiques globales</h3>
        <button class="btn" (click)="loadGlobal()">Rafraîchir</button>
        <pre style="margin-top:12px; background:#fafafa; padding:12px; border-radius:12px; overflow:auto">{{ globalStats | json }}</pre>
      </div>

      <div class="card">
        <h3>Statistiques par site</h3>
        <div style="margin-top:8px">
          <label>Site</label>
          <select [(ngModel)]="selectedSiteId" style="margin-top:6px" (change)="loadSite()">
            <option [ngValue]="null">-- Choisir --</option>
            <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
          </select>
        </div>

        <pre style="margin-top:12px; background:#fafafa; padding:12px; border-radius:12px; overflow:auto">{{ siteStats | json }}</pre>
      </div>
    </div>
  `
})
export class AdminDashboardPageComponent {
  globalStats: any = null;
  siteStats: any = null;
  sites: Site[] = [];
  selectedSiteId: number | null = null;

  constructor(
    public auth: AdminAuthService,
    private api: PadelApiService,
    private router: Router
  ) {
    if (!this.auth.isLoggedIn()) {
      this.router.navigateByUrl('/admin');
      return;
    }
    this.api.getSites().subscribe({ next: s => this.sites = s });
    this.loadGlobal();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigateByUrl('/admin');
  }

  loadGlobal(): void {
    this.api.getStatsGlobales().subscribe({ next: s => this.globalStats = s, error: () => this.globalStats = { error: 'Accès refusé ou backend non démarré' } });
  }

  loadSite(): void {
    this.siteStats = null;
    if (!this.selectedSiteId) return;
    this.api.getStatsSite(this.selectedSiteId).subscribe({ next: s => this.siteStats = s, error: () => this.siteStats = { error: 'Accès refusé ou backend non démarré' } });
  }
}
