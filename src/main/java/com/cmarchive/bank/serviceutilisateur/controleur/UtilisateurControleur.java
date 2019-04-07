package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UtilisateurControleur {

    private UtilisateurService utilisateurService;

    public UtilisateurControleur(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    public Utilisateurs listerUtilisateur() {
        return utilisateurService.listerUtilisateurs();
    }

    @GetMapping("/utilisateurs/{id}")
    public Utilisateur recupererUtilisateur(@PathVariable String id) {
        return utilisateurService.recupererUtilisateur(id);
    }

    @PostMapping("/utilisateurs")
    @ResponseStatus(HttpStatus.CREATED)
    public Utilisateur sauvegarderUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.sauvegarderUtilisateur(utilisateur);
    }

    @PutMapping("/utilisateurs")
    public Utilisateur modifierUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.sauvegarderUtilisateur(utilisateur);
    }

    @DeleteMapping("/utilisateurs")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerUtilisateur(@RequestBody Utilisateur utilisateur) {
        utilisateurService.supprimerUtilisateur(utilisateur);
    }
}
