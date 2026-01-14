import { Component, computed, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AdminAuthService } from './services/admin-auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="topbar">
      <div class="container">
        <div class="flex items-center gap-2">
          <strong class="text-xl font-bold text-gray-900">PadelService</strong>
          <span class="text-sm text-gray-500">(Frontend Angular)</span>
        </div>
        <div class="nav">
          <a routerLink="/" class="px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors">Accueil</a>
          <a routerLink="/matches-publics" class="px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors">Matches publics</a>
          <a routerLink="/mes-reservations" class="px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors">Mes réservations</a>
          <a routerLink="/admin" class="px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors">Admin</a>
        </div>
      </div>
    </div>

    <div class="container py-6">
      <router-outlet />
      <div class="mt-6 text-xs text-gray-500">
        Backend: {{ apiBase() }}
      </div>
    </div>
  `
})
export class AppComponent {
  apiBase = signal('http://localhost:8080');
  constructor(public adminAuth: AdminAuthService) {}
}
