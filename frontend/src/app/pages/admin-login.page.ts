import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminAuthService } from '../services/admin-auth.service';
import { PadelApiService } from '../services/padel-api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>Admin</h2>

    <div class="card" style="max-width:520px">
      <p style="color:#666">Authentification HTTP Basic. Identifiants d'exemple (seed): <code>AG0001</code> / <code>admin123</code>.</p>

      <label>Matricule admin</label>
      <input [(ngModel)]="username" placeholder="AG0001"/>

      <label style="margin-top:10px">Mot de passe</label>
      <input type="password" [(ngModel)]="password" placeholder="admin123"/>

      <button class="btn" style="margin-top:12px" (click)="login()">Se connecter</button>
      <button class="btn secondary" style="margin-top:12px; margin-left:8px" (click)="logout()" *ngIf="auth.isLoggedIn()">Déconnexion</button>

      <div *ngIf="error" class="error" style="margin-top:12px">{{ error }}</div>
    </div>
  `
})
export class AdminLoginPageComponent {
  username = '';
  password = '';
  error: string | null = null;

  constructor(
    public auth: AdminAuthService,
    private api: PadelApiService,
    private router: Router
  ) {}

  login(): void {
    this.error = null;
    if (!this.username.trim() || !this.password) {
      this.error = 'Veuillez entrer un matricule et un mot de passe.';
      return;
    }

    this.auth.setCredentials(this.username.trim(), this.password);
    // Test rapide via /api/auth/me
    this.api.getStatsGlobales().subscribe({
      next: () => this.router.navigateByUrl('/admin/dashboard'),
      error: () => {
        this.auth.logout();
        this.error = 'Échec login (credentials invalides ou backend non démarré).';
      }
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigateByUrl('/');
  }
}
