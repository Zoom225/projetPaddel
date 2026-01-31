import { HttpInterceptorFn } from '@angular/common/http';

export const adminAuthInterceptor: HttpInterceptorFn = (req, next) => {
  // On envoie l'auth pour les endpoints d'administration:
  // - /api/auth (GET)
  // - Mutations (POST/PUT/DELETE) sur /api/sites, /api/terrains, /api/membres, /api/statistiques
  // Note: Les GET sur /api/statistiques sont maintenant publics
  const isMutation = req.method !== 'GET';
  const needsAuth =
    req.url.includes('/api/auth') ||
    (isMutation && (req.url.includes('/api/sites') || req.url.includes('/api/terrains') || req.url.includes('/api/membres') || req.url.includes('/api/statistiques')));
  if (!needsAuth) return next(req);

  const token = sessionStorage.getItem('padel_admin_basic');
  if (!token) return next(req);

  const authReq = req.clone({
    setHeaders: {
      Authorization: `Basic ${token}`
    }
  });
  return next(authReq);
};
