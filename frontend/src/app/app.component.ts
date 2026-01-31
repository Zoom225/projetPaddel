import { Component, computed, signal } from '@angular/core';
import { RouterLink, RouterOutlet, RouterLinkActive } from '@angular/router';
import { AdminAuthService } from './services/admin-auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  template: `
    <div class="min-h-screen flex flex-col bg-slate-50">
      <!-- Navbar -->
      <nav class="sticky top-0 z-50 bg-padel-navy/95 backdrop-blur-md border-b border-white/10 shadow-lg">
        <div class="container mx-auto">
          <div class="flex items-center justify-between h-16">
            <!-- Logo -->
            <a routerLink="/" class="flex items-center gap-3 group">
              <div class="w-8 h-8 rounded-full bg-gradient-to-tr from-padel-green to-teal-400 flex items-center justify-center shadow-[0_0_15px_rgba(132,204,22,0.5)] group-hover:scale-110 transition-transform">
                <span class="text-padel-navy font-bold text-xs">P</span>
              </div>
              <div>
                <span class="text-white font-bold text-xl tracking-tight">Padel<span class="text-padel-green">Service</span></span>
              </div>
            </a>

            <!-- Navigation Items -->
            <div class="hidden md:flex items-center gap-1">
              <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}" class="nav-link">Accueil</a>
              <a routerLink="/matches-publics" routerLinkActive="active" class="nav-link">Matches <span class="bg-padel-green/20 text-padel-green text-[10px] px-1.5 py-0.5 rounded ml-1">Live</span></a>
              <a routerLink="/mes-reservations" routerLinkActive="active" class="nav-link">Mes Réservations</a>
              <a routerLink="/admin" routerLinkActive="active" class="nav-link">Admin</a>
            </div>
            
            <!-- Mobile Menu Button (Placeholder) -->
            <button class="md:hidden text-white">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
            </button>
          </div>
        </div>
      </nav>

      <!-- Main Content -->
      <main class="flex-grow container py-8 animate-fade-in">
        <router-outlet />
      </main>

      <!-- Footer -->
      <footer class="bg-white border-t border-slate-200 mt-auto">
        <div class="container py-6">
          <div class="flex justify-between items-center text-sm text-slate-500">
            <div>
              &copy; 2026 PadelService. Tous droits réservés.
            </div>
            <div class="flex items-center gap-2">
              <span class="w-2 h-2 rounded-full bg-green-500 animate-pulse"></span>
              Backend: {{ apiBase() }}
            </div>
          </div>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    :host { display: block; }
    .animate-fade-in { animation: fadeIn 0.5s ease-out; }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
  `]
})
export class AppComponent {
  apiBase = signal('http://localhost:8080'); // URL Backend
  constructor(public adminAuth: AdminAuthService) { }
}
