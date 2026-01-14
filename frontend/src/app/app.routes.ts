import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home.page';
import { MyReservationsPageComponent } from './pages/my-reservations.page';
import { PublicMatchesPageComponent } from './pages/public-matches.page';
import { AdminLoginPageComponent } from './pages/admin-login.page';
import { AdminDashboardPageComponent } from './pages/admin-dashboard.page';
import { AdminSitesPageComponent } from './pages/admin-sites.page';
import { AdminTerrainsPageComponent } from './pages/admin-terrains.page';
import { AdminMembresPageComponent } from './pages/admin-membres.page';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'matches-publics', component: PublicMatchesPageComponent },
  { path: 'mes-reservations', component: MyReservationsPageComponent },
  { path: 'admin', component: AdminLoginPageComponent },
  { path: 'admin/dashboard', component: AdminDashboardPageComponent },
  { path: 'admin/sites', component: AdminSitesPageComponent },
  { path: 'admin/terrains', component: AdminTerrainsPageComponent },
  { path: 'admin/membres', component: AdminMembresPageComponent },
  { path: '**', redirectTo: '' }
];
