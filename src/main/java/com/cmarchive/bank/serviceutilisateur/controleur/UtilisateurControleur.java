package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class UtilisateurControleur {

    private UtilisateurService utilisateurService;

    public UtilisateurControleur(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    public Flux<UtilisateurDto> listerUtilisateur() {
        return utilisateurService.listerUtilisateurs();
    }

    @GetMapping("/utilisateurs/{id}")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    public Mono<UtilisateurDto> recupererUtilisateur(@PathVariable String id) {
        return utilisateurService.recupererUtilisateur(id);
    }

    @PostMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UtilisateurDto> sauvegarderUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        try {
            return utilisateurService.creerUtilisateur(utilisateurDto);
        } catch (UtilisateurDejaPresentException udpe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, udpe.getMessage(), udpe);
        }
    }

    @PutMapping("/utilisateurs")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    public Mono<UtilisateurDto> modifierUtilisateur(@RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.modifierUtilisateur(utilisateurDto);
    }

    @DeleteMapping("/utilisateurs/{id}")
    @PreAuthorize("#oauth2.hasScope('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> supprimerUtilisateur(@PathVariable String id) {
        return utilisateurService.supprimerUtilisateur(id);
    }
}
