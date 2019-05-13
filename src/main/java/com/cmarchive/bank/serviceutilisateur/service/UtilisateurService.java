package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;

/**
 * Service CRUD utilisateur.
 */
public interface UtilisateurService {

    UtilisateursDto listerUtilisateurs();
    UtilisateurDto recupererUtilisateurParEmail(String email);
    UtilisateurDto recupererUtilisateur(String id);
    UtilisateurDto creerUtilisateur(UtilisateurDto utilisateurDto);
    UtilisateurDto modifierUtilisateur(UtilisateurDto utilisateurDto);
    void supprimerUtilisateur(UtilisateurDto utilisateur);
}
