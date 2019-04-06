package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;

/**
 * Service CRUD utilisateur.
 */
public interface UtilisateurService {

    Utilisateurs listerUtilisateurs();
    Utilisateur recupererUtilisateur(String id);
    Utilisateur sauvegarderUtilisateur(Utilisateur utilisateur);
    void supprimerUtilisateur(Utilisateur utilisateur);
}
