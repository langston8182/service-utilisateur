package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.api.UtilisateursApi;
import com.cmarchive.bank.ressource.model.*;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UtilisateurControleur implements UtilisateursApi {

    private UtilisateurService utilisateurService;
    private OperationService operationService;
    private OperationPermanenteService operationPermanenteService;

    public UtilisateurControleur(UtilisateurService utilisateurService,
                                 OperationService operationService,
                                 OperationPermanenteService operationPermanenteService) {
        this.utilisateurService = utilisateurService;
        this.operationService = operationService;
        this.operationPermanenteService = operationPermanenteService;
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDtos> listerUtilisateur() {
        UtilisateurDtos utilisateurDtos = utilisateurService.listerUtilisateurs();
        utilisateurDtos.getUtilisateurDtos()
                .forEach(this::creerHateoasUtilisateurRel);
        utilisateurDtos.add(linkTo(methodOn(UtilisateursApi.class).listerUtilisateur()).withSelfRel());

        return new ResponseEntity<>(utilisateurDtos, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> recupererUtilisateur(@PathVariable String id) {
        try {
            UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateur(id);
            creerHateoasUtilisateurRel(utilisateurDto);
            utilisateurDto.add(creerHateoasListeUtilisateur());
            return new ResponseEntity<>(utilisateurDto, HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> recupererUtilisateurParEmail(@RequestParam String email) {
        try {
            UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateurParEmail(email);
            creerHateoasUtilisateurRel(utilisateurDto);
            utilisateurDto.add(creerHateoasListeUtilisateur());
            return new ResponseEntity<>(utilisateurDto, HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> modifierUtilisateur(@PathVariable String id, @Valid UtilisateurDto utilisateurDto) {
        try {
            UtilisateurDto resultat = utilisateurService.modifierUtilisateur(id, utilisateurDto);
            creerHateoasUtilisateurRel(resultat);
            resultat.add(creerHateoasListeUtilisateur());
            return new ResponseEntity<>(resultat, HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<UtilisateurDto> sauvegarderUtilisateur(@Valid UtilisateurDto utilisateurDto) {
        try {
            UtilisateurDto resultat = utilisateurService.creerUtilisateur(utilisateurDto);
            creerHateoasUtilisateurRel(resultat);
            resultat.add(creerHateoasListeUtilisateur());
            return new ResponseEntity<>(resultat, HttpStatus.CREATED);
        } catch (UtilisateurDejaPresentException udpe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, udpe.getMessage(), udpe);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable String id) {
        utilisateurService.supprimerUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationPermanenteDtos> listerOperationPermanenteUtilisateur(String idUtilisateur) {
        OperationPermanenteDtos operationPermanenteDtos =
                operationPermanenteService.listerOperationPermanentesParUtilisateur(idUtilisateur);
        operationPermanenteDtos.getOperationPermanenteDtos()
                .forEach(this::creerHateoasOperationPermanenteRel);
        operationPermanenteDtos.add(linkTo(methodOn(UtilisateursApi.class).listerOperationPermanenteUtilisateur(idUtilisateur)).withSelfRel());

        return new ResponseEntity<>(operationPermanenteDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OperationPermanenteDto> recupererOperationPermanenteParUtilisateur(String idUtilisateur, String idOperationPermanente) {
        try {
            OperationPermanenteDto operationPermanenteDto =
                    operationPermanenteService.recupererOperationPermanenteParUtilisateur(idUtilisateur, idOperationPermanente);
            creerHateoasOperationPermanenteRel(operationPermanenteDto);
            operationPermanenteDto.add(creerHateoasListeOperationPermanente(idUtilisateur));
            return new ResponseEntity<>(operationPermanenteDto, HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationDtos> listerOperationUtilisateur(String idUtilisateur) {
        OperationDtos operationDtos = operationService.listerOperationsParUtilisateur(idUtilisateur);
        operationDtos.getOperationDtos()
                .forEach(this::creerHateoasOperationRel);
        operationDtos.add(linkTo(methodOn(UtilisateursApi.class).listerOperationUtilisateur(idUtilisateur)).withSelfRel());

        return new ResponseEntity<>(operationDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OperationDto> recupererOperationParUtilisateur(String idUtilisateur, String idOperation) {
        try {
            OperationDto operationDto = operationService.recupererOperationParUtilisateur(idUtilisateur, idOperation);
            creerHateoasOperationRel(operationDto);
            operationDto.add(creerHateoasListeOperation(idUtilisateur));
            return new ResponseEntity<>(operationDto, HttpStatus.OK);
        } catch (UtilisateurNonTrouveException unte) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, unte.getMessage(), unte);
        }
    }

    private void creerHateoasOperationPermanenteRel(OperationPermanenteDto operationPermanenteDto) {
        operationPermanenteDto.add(creerHateoasOperationPermanenteSelf(operationPermanenteDto.getUtilisateurDto().getIdentifiant(),
                operationPermanenteDto.getIdentifiant()));
        operationPermanenteDto.add(linkTo(methodOn(UtilisateursApi.class)
                .recupererUtilisateur(
                        operationPermanenteDto.getUtilisateurDto().getIdentifiant())).withRel("utilisateur").withType("GET"));
    }

    private Link creerHateoasOperationPermanenteSelf(String idUtilisateur, String idOperationPermanente) {
        return linkTo(methodOn(UtilisateursApi.class)
                .recupererOperationPermanenteParUtilisateur(idUtilisateur, idOperationPermanente)).withSelfRel().withType("GET");
    }

    private void creerHateoasOperationRel(OperationDto operationDto) {
        operationDto.add(creerHateoasOperationSelf(operationDto.getUtilisateurDto().getIdentifiant(), operationDto.getIdentifiant()));
        operationDto.add(linkTo(methodOn(UtilisateursApi.class)
                .recupererUtilisateur(
                        operationDto.getUtilisateurDto().getIdentifiant())).withRel("utilisateur").withType("GET"));
    }

    private Link creerHateoasOperationSelf(String idUtilisateur, String idOperation) {
        return linkTo(methodOn(UtilisateursApi.class)
                .recupererOperationParUtilisateur(idUtilisateur, idOperation)).withSelfRel().withType("GET");
    }

    private void creerHateoasUtilisateurRel(UtilisateurDto utilisateurDto) {
        utilisateurDto.add(creerHateoasUtilisateurSelf(utilisateurDto.getIdentifiant()));
        utilisateurDto.add(creerHateoasListeOperation(utilisateurDto.getIdentifiant()));
        utilisateurDto.add(creerHateoasListeOperationPermanente(utilisateurDto.getIdentifiant()));
    }

    private Link creerHateoasUtilisateurSelf(String idUtilisateur) {
        return linkTo(methodOn(UtilisateursApi.class).recupererUtilisateur(idUtilisateur)).withSelfRel().withType("GET");
    }

    private Link creerHateoasListeUtilisateur() {
        return linkTo(methodOn(UtilisateursApi.class).listerUtilisateur()).withRel("utilisateurs").withType("GET");
    }

    private Link creerHateoasListeOperation(String idUtilisateur) {
        return linkTo(methodOn(UtilisateursApi.class).listerOperationUtilisateur(idUtilisateur)).withRel("operations").withType("GET");
    }

    private Link creerHateoasListeOperationPermanente(String idUtilisateur) {
        return linkTo(methodOn(UtilisateursApi.class).listerOperationPermanenteUtilisateur(idUtilisateur))
                .withRel("operations-permanentes").withType("GET");
    }
}
