package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class OperationControleur {

    private OperationService operationService;

    public OperationControleur(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/operations")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public OperationsDto listerOperationUtilisateur(Principal principal) {
        return operationService.listerOperationsParUtilisateur(principal.getName());
    }

    @PostMapping("/operations")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationDto ajouterOperationAUtilisateur(Principal principal,
                                                     @RequestBody OperationDto operationDto) {
        return operationService.ajouterOperationAUtilisateur(principal.getName(), operationDto);
    }

    @PutMapping("/operations")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public OperationDto modifierOperationUtilisateur(@RequestBody OperationDto operationDto) {
        return operationService.modifierOperationUtilisateur(operationDto);
    }

    @DeleteMapping("/operations")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerOperationUtilisateur(@RequestBody OperationDto operationDto) {
        operationService.supprimerOperation(operationDto);
    }
}
