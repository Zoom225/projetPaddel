export interface Site {
  id: number;
  nom: string;
  heureDebut: string;
  heureFin: string;
  dureeMatchMinutes: number;
  dureeEntreMatchMinutes: number;
}

export interface Terrain {
  id: number;
  nom: string;
  site: Site;
}

export interface Membre {
  id: number;
  matricule: string;
  nom: string;
  prenom: string;
  email: string;
  typeMembre: string;
  soldeDu: number;
  site?: Site;
}

export interface Match {
  id: number;
  date: string;
  heureDebut: string;
  heureFin: string;
  typeMatch: 'PRIVE' | 'PUBLIC';
  terrain: Terrain;
  organisateur: Membre;
  joueurs: Membre[];
  prixTotal: number;
  prixParJoueur: number;
  estComplet: boolean;
  estPaye: boolean;
}

export interface Paiement {
  id: number;
  montant: number;
  datePaiement: string;
  estValide: boolean;
}
