import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Match, Site, Terrain } from './api';

@Injectable({ providedIn: 'root' })
export class ApiService {
  readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getSites(): Observable<Site[]> {
    return this.http.get<Site[]>(`${this.baseUrl}/sites`);
  }

  getTerrainsBySite(siteId: number): Observable<Terrain[]> {
    return this.http.get<Terrain[]>(`${this.baseUrl}/terrains/site/${siteId}`);
  }

  getPublicMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.baseUrl}/matches/public`);
  }

  getMatchesByMembre(matricule: string): Observable<Match[]> {
    return this.http.get<Match[]>(`${this.baseUrl}/matches/membre/${encodeURIComponent(matricule)}`);
  }

  createMatch(payload: { matriculeOrganisateur: string; terrainId: number; date: string; heureDebut: string; typeMatch: string }): Observable<Match> {
    let params = new HttpParams()
      .set('matriculeOrganisateur', payload.matriculeOrganisateur)
      .set('terrainId', payload.terrainId)
      .set('date', payload.date)
      .set('heureDebut', payload.heureDebut)
      .set('typeMatch', payload.typeMatch);

    return this.http.post<Match>(`${this.baseUrl}/matches`, null, { params });
  }

  payer(matchId: number, matricule: string): Observable<any> {
    let params = new HttpParams().set('matricule', matricule);
    return this.http.post(`${this.baseUrl}/matches/${matchId}/paiement`, null, { params });
  }
}
