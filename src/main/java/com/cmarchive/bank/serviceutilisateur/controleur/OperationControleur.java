package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class OperationControleur {

    private OperationService operationService;

    public OperationControleur(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/operations/{utilisateurId}")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public OperationsDto listerOperationUtilisateur(@PathVariable String utilisateurId) {
        return operationService.listerOperationsParUtilisateur(utilisateurId);
    }

    @PostMapping("/operations/{utilisateurId}")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationDto ajouterOperationAUtilisateur(@PathVariable String utilisateurId,
                                                     @RequestBody OperationDto operationDto) {
        return operationService.ajouterOperationAUtilisateur(utilisateurId, operationDto);
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
