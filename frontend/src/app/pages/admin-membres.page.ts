import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdminAuthService } from '../services/admin-auth.service';
import { PadelApiService } from '../services/padel-api.service';
import { Site } from '../services/api';

type TypeMembre = 'GLOBAL' | 'SITE' | 'LIBRE';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="row" style="justify-content:space-between; align-items:center; gap:12px">
      <h2 style="margin:0">Administration • Membres</h2>
      <div class="row" style="gap:8px">
        <a class="btn btn-secondary" routerLink="/admin/dashboard">Dashboard</a>
        <a class="btn btn-secondary" routerLink="/admin/sites">Sites</a>
        <a class="btn btn-secondary" routerLink="/admin/terrains">Terrains</a>
        <button class="btn" (click)="logout()">Déconnexion</button>
      </div>
    </div>

    <div class="card" *ngIf="!auth.isLoggedIn()">
      <div class="error">Vous devez vous authentifier d'abord.</div>
      <a class="btn" routerLink="/admin">Aller au login</a>
    </div>

    <div class="grid grid-2" *ngIf="auth.isLoggedIn()">
      <div class="card">
        <h3>Créer un membre</h3>

        <label>Type</label>
        <select [(ngModel)]="createType" (change)="onTypeChange()">
          <option value="GLOBAL">GLOBAL (matricule commence par G)</option>
          <option value="SITE">SITE (matricule commence par S)</option>
          <option value="LIBRE">LIBRE (matricule commence par L)</option>
        </select>

        <label style="margin-top:8px">Matricule</label>
        <input [(ngModel)]="createMatricule" placeholder="G0001 / S00001 / L00001" />

        <div class="grid grid-2" style="margin-top:8px">
          <div>
            <label>Nom</label>
            <input [(ngModel)]="createNom" />
          </div>
          <div>
            <label>Prénom</label>
            <input [(ngModel)]="createPrenom" />
          </div>
        </div>

        <label style="margin-top:8px">Email</label>
        <input [(ngModel)]="createEmail" placeholder="ex: membre@mail.com" />

        <div *ngIf="createType === 'SITE'" style="margin-top:8px">
          <label>Site (obligatoire pour SITE)</label>
          <select [(ngModel)]="createSiteId" style="margin-top:6px">
            <option [ngValue]="null">-- Choisir --</option>
            <option *ngFor="let s of sites" [ngValue]="s.id">{{ s.nom }}</option>
          </select>
        </div>

        <button class="btn" style="margin-top:12px" (click)="createMembre()">Créer</button>
        <div class="hint" style="margin-top:10px" *ngIf="message">{{ message }}</div>
      </div>

      <div class="card">
        <div class="row" style="justify-content:space-between; align-items:center">
          <h3 style="margin:0">Liste des membres</h3>
          <button class="btn btn-secondary" (click)="refresh()">Rafraîchir</button>
        </div>

        <div class="row" style="gap:8px; margin-top:10px; align-items:center">
          <label style="margin:0">Recherche</label>
          <input [(ngModel)]="q" (input)="applyFilter()" placeholder="matricule / nom / email..." />
        </div>

        <table style="width:100%; margin-top:10px">
          <thead>
            <tr>
              <th>ID</th>
              <th>Matricule</th>
              <th>Nom</th>
              <th>Prénom</th>
              <th>Email</th>
              <th>Type</th>
              <th>Site</th>
              <th>Solde dû</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let m of filtered">
              <td>{{ m.id }}</td>
              <td><code>{{ m.matricule }}</code></td>
              <td><input [(ngModel)]="m.nom" /></td>
              <td><input [(ngModel)]="m.prenom" /></td>
              <td><input [(ngModel)]="m.email" /></td>
              <td><code>{{ m.typeMembre }}</code></td>
              <td>
                <span *ngIf="!m.site">-</span>
                <span *ngIf="m.site">{{ m.site.nom }}</span>
              </td>
              <td>{{ m.soldeDu }}</td>
              <td style="white-space:nowrap">
                <button class="btn btn-secondary" (click)="saveMembre(m)">Enregistrer</button>
                <button class="btn" style="margin-left:6px" (click)="deleteMembre(m)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>

        <div class="hint" style="margin-top:10px">
          Les règles de matricule sont validées côté backend (G/S/L selon le type).
        </div>
      </div>
    </div>
  `
})
export class AdminMembresPageComponent {
  sites: Site[] = [];
  membres: any[] = [];
  filtered: any[] = [];
  q = '';

  createType: TypeMembre = 'GLOBAL';
  createMatricule = '';
  createNom = '';
  createPrenom = '';
  createEmail = '';
  createSiteId: number | null = null;

  message = '';

  constructor(
    public auth: AdminAuthService,
    private api: PadelApiService,
    private router: Router
  ) {
    if (!this.auth.isLoggedIn()) return;
    this.api.getSites().subscribe({ next: s => (this.sites = s as any[]) });
    this.refresh();
  }

  onTypeChange(): void {
    if (this.createType !== 'SITE') this.createSiteId = null;
  }

  refresh(): void {
    this.api.getMembres().subscribe({
      next: ms => {
        this.membres = ms as any[];
        this.applyFilter();
      },
      error: () => (this.message = 'Erreur: backend non démarré ou accès refusé.')
    });
  }

  applyFilter(): void {
    const q = (this.q || '').trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.membres];
      return;
    }
    this.filtered = this.membres.filter(m =>
      String(m.matricule || '').toLowerCase().includes(q) ||
      String(m.nom || '').toLowerCase().includes(q) ||
      String(m.prenom || '').toLowerCase().includes(q) ||
      String(m.email || '').toLowerCase().includes(q)
    );
  }

  createMembre(): void {
    this.message = '';
    const matricule = this.createMatricule.trim();
    const nom = this.createNom.trim();
    const prenom = this.createPrenom.trim();
    const email = this.createEmail.trim();

    if (!matricule || !nom || !prenom || !email) {
      this.message = 'Tous les champs (matricule/nom/prénom/email) sont obligatoires.';
      return;
    }
    if (this.createType === 'SITE' && !this.createSiteId) {
      this.message = 'Le site est obligatoire pour un membre SITE.';
      return;
    }

    const payload: any = {
      matricule,
      nom,
      prenom,
      email,
      typeMembre: this.createType,
      soldeDu: 0.0
    };
    if (this.createType === 'SITE') payload.site = { id: this.createSiteId };

    this.api.createMembre(payload).subscribe({
      next: () => {
        this.message = 'Membre créé.';
        this.createMatricule = '';
        this.createNom = '';
        this.createPrenom = '';
        this.createEmail = '';
        this.createSiteId = null;
        this.refresh();
      },
      error: () => (this.message = 'Création impossible (droits admin requis ou matricule invalide).')
    });
  }

  saveMembre(m: any): void {
    if (!m.id) return;
    const payload: any = { nom: m.nom, prenom: m.prenom, email: m.email };
    this.api.updateMembre(m.id, payload).subscribe({
      next: () => (this.message = 'Membre mis à jour.'),
      error: () => (this.message = 'Mise à jour impossible (droits admin requis).')
    });
  }

  deleteMembre(m: any): void {
    if (!m.id) return;
    if (!confirm(`Supprimer le membre "${m.matricule}" ?`)) return;
    this.api.deleteMembre(m.id).subscribe({
      next: () => {
        this.message = 'Membre supprimé.';
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
