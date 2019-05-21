package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
public class UtilisateurControleur {

    private UtilisateurService utilisateurService;

    public UtilisateurControleur(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateurs/")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public UtilisateursDto listerUtilisateur() {
        System.out.println();
        return utilisateurService.listerUtilisateurs();
    }

    @GetMapping("/utilisateurs/{id}")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public UtilisateurDto recupererUtilisateur(@PathVariable String id) {
        try {
            return utilisateurService.recupererUtilisateur(id);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @GetMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public UtilisateurDto recupererUtilisateurParEmail(@RequestParam String email) {
        try {
            return utilisateurService.recupererUtilisateurParEmail(email);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PostMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurDto sauvegarderUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        try {
            return utilisateurService.creerUtilisateur(utilisateurDto);
        } catch (UtilisateurDejaPresentException udpe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, udpe.getMessage(), udpe);
        }
    }

    @PutMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public UtilisateurDto modifierUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        try {
            return utilisateurService.modifierUtilisateur(utilisateurDto);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @DeleteMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        utilisateurService.supprimerUtilisateur(utilisateurDto);
    }
}
