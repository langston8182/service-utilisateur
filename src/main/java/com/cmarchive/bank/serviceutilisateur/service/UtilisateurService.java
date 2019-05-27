package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.ressource.model.UtilisateurDtos;

/**
 * Service CRUD utilisateur.
 */
public interface UtilisateurService {

    UtilisateurDtos listerUtilisateurs();
    UtilisateurDto recupererUtilisateurParEmail(String email);
    UtilisateurDto recupererUtilisateur(String id);
    UtilisateurDto creerUtilisateur(UtilisateurDto utilisateurDto);
    UtilisateurDto modifierUtilisateur(String idUtilisateur, UtilisateurDto utilisateurDto);
    void supprimerUtilisateur(String id);
}
