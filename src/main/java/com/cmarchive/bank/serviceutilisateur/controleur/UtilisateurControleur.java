package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.api.UtilisateursApi;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.ressource.model.UtilisateurDtos;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UtilisateurControleur implements UtilisateursApi {

    private UtilisateurService utilisateurService;

    public UtilisateurControleur(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDtos> listerUtilisateur() {
        return new ResponseEntity(utilisateurService.listerUtilisateurs(), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> recupererUtilisateur(@PathVariable String id) {
        try {
            return new ResponseEntity(utilisateurService.recupererUtilisateur(id), HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> recupererUtilisateurParEmail(@RequestParam String email) {
        try {
            return new ResponseEntity(utilisateurService.recupererUtilisateurParEmail(email), HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> modifierUtilisateur(@Valid UtilisateurDto utilisateurDto) {
        try {
            return new ResponseEntity(utilisateurService.modifierUtilisateur(utilisateurDto), HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> sauvegarderUtilisateur(@Valid UtilisateurDto utilisateurDto) {
        try {
            return new ResponseEntity(utilisateurService.creerUtilisateur(utilisateurDto), HttpStatus.CREATED);
        } catch (UtilisateurDejaPresentException udpe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, udpe.getMessage(), udpe);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<Void> supprimerUtilisateur(@Valid UtilisateurDto utilisateurDto) {
        utilisateurService.supprimerUtilisateur(utilisateurDto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
