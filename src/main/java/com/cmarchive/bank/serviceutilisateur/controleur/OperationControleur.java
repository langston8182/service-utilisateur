package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.api.OperationsApi;
import com.cmarchive.bank.ressource.model.OperationDto;
import com.cmarchive.bank.ressource.model.OperationDtos;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class OperationControleur implements OperationsApi {

    private OperationService operationService;
    private HttpServletRequest httpServletRequest;

    public OperationControleur(OperationService operationService, HttpServletRequest httpServletRequest) {
        this.operationService = operationService;
        this.httpServletRequest = httpServletRequest;
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationDtos> listerOperationUtilisateur() {
        Principal principal = httpServletRequest.getUserPrincipal();
        return new ResponseEntity(operationService.listerOperationsParUtilisateur(principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationDto> ajouterOperationAUtilisateur(@RequestBody OperationDto operationDto) {
        Principal principal = httpServletRequest.getUserPrincipal();
        return new ResponseEntity(operationService.ajouterOperationAUtilisateur(principal.getName(), operationDto), HttpStatus.CREATED);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<OperationDto> modifierOperationUtilisateur(@RequestBody OperationDto operationDto) {
        return new ResponseEntity(operationService.modifierOperationUtilisateur(operationDto), HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('openid')")
    @Override
    public ResponseEntity<Void> supprimerOperationUtilisateur(@RequestBody OperationDto operationDto) {
        operationService.supprimerOperation(operationDto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
