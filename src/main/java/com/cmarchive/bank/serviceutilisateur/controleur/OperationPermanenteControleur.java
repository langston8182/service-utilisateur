package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.api.OperationsPermanentesApi;
import com.cmarchive.bank.ressource.model.OperationPermanenteDto;
import com.cmarchive.bank.ressource.model.OperationPermanenteDtos;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/")
public class OperationPermanenteControleur implements OperationsPermanentesApi {

    private OperationPermanenteService operationPermanenteService;
    private HttpServletRequest httpServletRequest;

    public OperationPermanenteControleur(OperationPermanenteService operationPermanenteService, HttpServletRequest httpServletRequest) {
        this.operationPermanenteService = operationPermanenteService;
        this.httpServletRequest = httpServletRequest;
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationPermanenteDtos> listerOperationPermanenteUtilisateur() {
        Principal principal = httpServletRequest.getUserPrincipal();
        return new ResponseEntity(operationPermanenteService.listerOperationPermanentesParUtilisateur(principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationPermanenteDto> ajouterOperationPermanenteAUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        Principal principal = httpServletRequest.getUserPrincipal();
        return new ResponseEntity(operationPermanenteService.ajouterOperationPermanenteAUtilisateur(principal.getName(),
                operationPermanenteDto), HttpStatus.CREATED);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationPermanenteDto> modifierOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        return new ResponseEntity(operationPermanenteService.modifierOperationPermanenteUtilisateur(operationPermanenteDto), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<Void> supprimerOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        operationPermanenteService.supprimerOperationPermanente(operationPermanenteDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
