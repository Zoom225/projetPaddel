import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AdminAuthService {
  private readonly KEY = 'padel_admin_basic';

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem(this.KEY);
  }

  setCredentials(username: string, password: string): void {
    const token = btoa(`${username}:${password}`);
    sessionStorage.setItem(this.KEY, token);
  }

  getAuthorizationHeader(): string | null {
    const token = sessionStorage.getItem(this.KEY);
    return token ? `Basic ${token}` : null;
  }

  logout(): void {
    sessionStorage.removeItem(this.KEY);
  }
}
