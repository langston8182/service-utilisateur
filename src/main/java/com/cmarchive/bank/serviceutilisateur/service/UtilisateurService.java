package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service CRUD utilisateur.
 */
@Service
public interface UtilisateurService {

    List<Utilisateur> listerUtilisateurs();
    Utilisateur recupererUtilisateur(String id);
    Utilisateur sauvegarderUtilisateur(Utilisateur utilisateur);
    void supprimerUtilisateur(Utilisateur utilisateur);
}
