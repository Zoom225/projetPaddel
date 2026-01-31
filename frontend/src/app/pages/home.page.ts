import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PadelApiService } from '../services/padel-api.service';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <!-- Hero Section -->
    <div class="mb-10 text-center space-y-4">
      <h1 class="heading-1 text-4xl md:text-5xl">Bienvenue sur <span class="text-transparent bg-clip-text bg-gradient-to-r from-padel-navy to-teal-600">PadelService</span></h1>
      <p class="text-xl text-slate-500 max-w-2xl mx-auto">
        La plateforme simplifiée pour gérer vos matches et réservations.
      </p>
    </div>

    <!-- Info Card -->
    <div class="card bg-gradient-to-r from-padel-navy to-slate-800 text-white border-none mb-10 overflow-hidden relative">
      <div class="absolute top-0 right-0 p-8 opacity-10">
        <svg class="w-32 h-32" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/></svg>
      </div>
      <div class="relative z-10 flex flex-col md:flex-row items-center gap-6">
        <div class="flex-1">
          <h3 class="text-xl font-bold text-padel-green mb-2">Comment ça marche ?</h3>
          <p class="text-slate-300">
            Les membres n'ont pas de login. Utilisez simplement votre <strong class="text-white">matricule</strong> (ex: G0001, S0001...) pour réserver ou rejoindre un match.
          </p>
        </div>
        <a routerLink="/matches-publics" class="btn btn-accent shadow-lg shadow-padel-green/20">
          Voir les matches publics
        </a>
      </div>
    </div>

    <!-- Sites Section -->
    <div class="flex items-center justify-between mb-6">
      <h2 class="heading-2 flex items-center gap-2">
        <span class="w-1.5 h-8 bg-padel-green rounded-full"></span>
        Nos Sites
      </h2>
      <span class="text-sm text-slate-400">{{ sites.length }} sites disponibles</span>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div class="card group cursor-pointer" *ngFor="let s of sites">
        <div class="h-32 bg-slate-100 rounded-xl mb-4 flex items-center justify-center relative overflow-hidden">
          <div class="absolute inset-0 bg-gradient-to-br from-slate-200 to-slate-100 group-hover:scale-105 transition-transform duration-500"></div>
          <span class="z-10 text-4xl transform group-hover:scale-110 transition-transform duration-300">🏟️</span>
        </div>
        
        <h3 class="text-lg font-bold text-padel-navy mb-1 group-hover:text-teal-600 transition-colors">{{s.nom}}</h3>
        
        <div class="space-y-3 mt-4">
          <div class="flex items-center justify-between text-sm">
            <span class="text-slate-500">Horaires</span>
            <span class="font-medium text-slate-700 bg-slate-100 px-2 py-1 rounded">{{s.heureDebut}} - {{s.heureFin}}</span>
          </div>
          <div class="flex items-center justify-between text-sm">
            <span class="text-slate-500">Durée match</span>
            <span class="font-medium text-slate-700">{{s.dureeMatchMinutes}} min</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div *ngIf="sites.length === 0" class="text-center py-12">
      <div class="animate-spin w-8 h-8 border-4 border-padel-green border-t-transparent rounded-full mx-auto mb-4"></div>
      <p class="text-slate-500">Chargement des sites...</p>
    </div>
  `
})
export class HomePageComponent {
  sites: any[] = [];

  constructor(private api: PadelApiService) {
    this.api.getSites().subscribe({
      next: s => (this.sites = s),
      error: () => (this.sites = [])
    });
  }
}
