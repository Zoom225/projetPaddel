import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match, Paiement, Site, Terrain } from './api';

@Injectable({ providedIn: 'root' })
export class PadelApiService {
  readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Sites
  getSites(): Observable<Site[]> {
    return this.http.get<Site[]>(`${this.baseUrl}/sites`);
  }

  createSite(site: Partial<Site>): Observable<Site> {
    return this.http.post<Site>(`${this.baseUrl}/sites`, site);
  }

  updateSite(id: number, site: Partial<Site>): Observable<Site> {
    return this.http.put<Site>(`${this.baseUrl}/sites/${id}`, site);
  }

  deleteSite(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/sites/${id}`);
  }

  getTerrainsBySite(siteId: number): Observable<Terrain[]> {
    return this.http.get<Terrain[]>(`${this.baseUrl}/terrains/site/${siteId}`);
  }

  getTerrains(): Observable<Terrain[]> {
    return this.http.get<Terrain[]>(`${this.baseUrl}/terrains`);
  }

  createTerrain(terrain: { nom: string; siteId: number }): Observable<Terrain> {
    return this.http.post<Terrain>(`${this.baseUrl}/terrains`, { nom: terrain.nom, site: { id: terrain.siteId } });
  }

  updateTerrain(id: number, terrain: { nom: string; siteId?: number | null }): Observable<Terrain> {
    const body: any = { nom: terrain.nom };
    if (terrain.siteId != null) body.site = { id: terrain.siteId };
    return this.http.put<Terrain>(`${this.baseUrl}/terrains/${id}`, body);
  }

  deleteTerrain(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/terrains/${id}`);
  }

  // Membres (admin)
  getMembres(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/membres`);
  }

  createMembre(membre: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/membres`, membre);
  }

  updateMembre(id: number, membre: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/membres/${id}`, membre);
  }

  deleteMembre(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/membres/${id}`);
  }

  // Matches
  getPublicMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.baseUrl}/matches/public`);
  }

  getMatchesByMembre(matricule: string): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.baseUrl}/matches/membre/${encodeURIComponent(matricule)}`);
  }

  createMatch(payload: {
    matriculeOrganisateur: string;
    terrainId: number;
    date: string;      // ISO date YYYY-MM-DD
    heureDebut: string; // HH:mm
    typeMatch: 'PRIVE' | 'PUBLIC';
  }): Observable<Match> {
    let params = new HttpParams()
      .set('matriculeOrganisateur', payload.matriculeOrganisateur)
      .set('terrainId', payload.terrainId)
      .set('date', payload.date)
      .set('heureDebut', payload.heureDebut)
      .set('typeMatch', payload.typeMatch);

    return this.http.post<Match>(`${this.baseUrl}/matches`, null, { params });
  }

  payer(matchId: number, matricule: string): Observable<Paiement> {
    const params = new HttpParams().set('matricule', matricule);
    return this.http.post<Paiement>(`${this.baseUrl}/matches/${matchId}/paiement`, null, { params });
  }

  // Admin - stats
  getStatsGlobales(): Observable<Record<string, any>> {
    return this.http.get<Record<string, any>>(`${this.baseUrl}/statistiques/globales`);
  }

  getStatsSite(siteId: number): Observable<Record<string, any>> {
    return this.http.get<Record<string, any>>(`${this.baseUrl}/statistiques/site/${siteId}`);
  }
}
