import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PadelApiService } from '../services/padel-api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2 class="text-3xl font-bold text-gray-900 mb-6">Accueil</h2>
    <div class="card mb-6">
      <p class="text-gray-700 mb-3">
        Cette application gère des sites de padel, des terrains, des matches (privés/publics) et des paiements.
      </p>
      <p class="text-gray-700">
        <strong class="font-semibold text-gray-900">Règle clé</strong> : les membres n'ont pas de login, ils utilisent leur <em class="italic">matricule</em> (G..., S..., L...).
      </p>
    </div>

    <h3 class="text-2xl font-semibold text-gray-900 mb-4">Sites</h3>
    <div class="grid grid-2">
      <div class="card hover:shadow-md transition-shadow" *ngFor="let s of sites">
        <div class="font-semibold text-lg text-gray-900 mb-2">{{s.nom}}</div>
        <div class="text-gray-600 text-sm">
          Horaires: {{s.heureDebut}} - {{s.heureFin}} | Match: {{s.dureeMatchMinutes}} min (+{{s.dureeEntreMatchMinutes}} min)
        </div>
      </div>
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
