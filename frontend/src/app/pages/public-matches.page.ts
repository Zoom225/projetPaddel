import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PadelApiService } from '../services/padel-api.service';
import { Match } from '../services/api';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>Matches publics</h2>

    <div class="card" *ngIf="error" style="margin-bottom:16px">
      <div class="error">{{ error }}</div>
    </div>

    <div class="grid">
      <div class="card" *ngFor="let m of matches">
        <div style="display:flex; justify-content:space-between; gap:12px; flex-wrap:wrap">
          <div>
            <strong>{{ m.siteNom }}</strong> — {{ m.terrainNom }}
            <div style="color:#666">{{ m.date }} · {{ m.heureDebut }} - {{ m.heureFin }}</div>
          </div>
          <div>
            <span style="padding:6px 10px; border-radius:999px; border:1px solid #e6e6e6; background:#fafafa">
              {{ m.typeMatch }}
            </span>
          </div>
        </div>

        <div style="margin-top:12px; color:#444">
          Joueurs: {{ m.nbJoueurs }}/4 · Payé: {{ m.estPaye ? 'oui' : 'non' }} · Complet: {{ m.estComplet ? 'oui' : 'non' }}
        </div>
      </div>
    </div>

    <div class="card" *ngIf="!error && matches.length === 0">
      Aucun match public.
    </div>
  `
})
export class PublicMatchesPageComponent {
  matches: Array<{
    id: number;
    date: string;
    heureDebut: string;
    heureFin: string;
    typeMatch: string;
    estComplet: boolean;
    estPaye: boolean;
    nbJoueurs: number;
    siteNom: string;
    terrainNom: string;
  }> = [];
  error: string | null = null;

  constructor(private api: PadelApiService) {
    this.load();
  }

  private load(): void {
    this.api.getPublicMatches().subscribe({
      next: (data: Match[]) => {
        this.matches = data.map(m => ({
          id: m.id,
          date: m.date,
          heureDebut: m.heureDebut,
          heureFin: m.heureFin,
          typeMatch: m.typeMatch,
          estComplet: m.estComplet,
          estPaye: m.estPaye,
          nbJoueurs: (m.joueurs || []).length,
          siteNom: m.terrain?.site?.nom ?? '-',
          terrainNom: m.terrain?.nom ?? '-'
        }));
      },
      error: () => this.error = "Impossible de charger les matches publics (backend non démarré ?)"
    });
  }
}
