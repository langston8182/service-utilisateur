package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanenteDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanentesDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class OperationPermanenteControleur {

    private OperationPermanenteService operationPermanenteService;

    public OperationPermanenteControleur(OperationPermanenteService operationPermanenteService) {
        this.operationPermanenteService = operationPermanenteService;
    }

    @GetMapping("/operations-permanentes")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public OperationPermanentesDto listerOperationPermanenteUtilisateur(Principal principal) {
        return operationPermanenteService.listerOperationPermanentesParUtilisateur(principal.getName());
    }

    @PostMapping("/operations-permanentes")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationPermanenteDto ajouterOperationPermanenteAUtilisateur(Principal principal,
                                                               @RequestBody OperationPermanenteDto operationPermanenteDto) {
        return operationPermanenteService.ajouterOperationPermanenteAUtilisateur(principal.getName(),
                operationPermanenteDto);
    }

    @PutMapping("/operations-permanentes")
    @PreAuthorize("#oauth2.hasScope('openid')")
    public OperationPermanenteDto modifierOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        return operationPermanenteService.modifierOperationPermanenteUtilisateur(operationPermanenteDto);
    }

    @DeleteMapping("/operations-permanentes")
    @PreAuthorize("#oauth2.hasScope('openid')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        operationPermanenteService.supprimerOperationPermanente(operationPermanenteDto);
    }
}
