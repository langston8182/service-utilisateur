package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanenteDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanentesDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class OperationPermanenteControleur {

    private OperationPermanenteService operationPermanenteService;

    public OperationPermanenteControleur(OperationPermanenteService operationPermanenteService) {
        this.operationPermanenteService = operationPermanenteService;
    }

    @GetMapping("/operations-permanentes/{utilisateurId}")
    @PreAuthorize("#oauth2.hasScope('USER')")
    public OperationPermanentesDto listerOperationPermanenteUtilisateur(@PathVariable String utilisateurId) {
        return operationPermanenteService.listerOperationPermanentesParUtilisateur(utilisateurId);
    }

    @PostMapping("/operations-permanentes/{utilisateurId}")
    @PreAuthorize("#oauth2.hasScope('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationPermanenteDto ajouterOperationPermanenteAUtilisateur(@PathVariable String utilisateurId,
                                                               @RequestBody OperationPermanenteDto operationPermanenteDto) {
        return operationPermanenteService.ajouterOperationPermanenteAUtilisateur(utilisateurId,
                operationPermanenteDto);
    }

    @PutMapping("/operations-permanentes")
    @PreAuthorize("#oauth2.hasScope('USER')")
    public OperationPermanenteDto modifierOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        return operationPermanenteService.modifierOperationPermanenteUtilisateur(operationPermanenteDto);
    }

    @DeleteMapping("/operations-permanentes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerOperationPermanenteUtilisateur(@RequestBody OperationPermanenteDto operationPermanenteDto) {
        operationPermanenteService.supprimerOperationPermanente(operationPermanenteDto);
    }
}
