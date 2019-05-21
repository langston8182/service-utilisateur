package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanenteDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanentesDto;

/**
 * Service pour les operations permanentes utilisateurs.
 */
public interface OperationPermanenteService {

    OperationPermanentesDto listerOperationPermanentesParUtilisateur(String email);
    OperationPermanenteDto ajouterOperationPermanenteAUtilisateur(
            String email, OperationPermanenteDto operationPermanenteDto);
    OperationPermanenteDto modifierOperationPermanenteUtilisateur(OperationPermanenteDto operationPermanenteDto);
    void supprimerOperationPermanente(OperationPermanenteDto operationPermanenteDto);

}
