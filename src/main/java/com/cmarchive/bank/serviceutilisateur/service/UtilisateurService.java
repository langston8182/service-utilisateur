package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;

/**
 * Service CRUD utilisateur.
 */
public interface UtilisateurService {

    UtilisateursDto listerUtilisateurs();
    UtilisateurDto recupererUtilisateur(String id);
    UtilisateurDto sauvegarderUtilisateur(UtilisateurDto utilisateur);
    void supprimerUtilisateur(UtilisateurDto utilisateur);
}
