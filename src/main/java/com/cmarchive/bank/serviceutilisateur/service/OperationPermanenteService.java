package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.OperationPermanenteDto;
import com.cmarchive.bank.ressource.model.OperationPermanenteDtos;

/**
 * Service pour les operations permanentes utilisateurs.
 */
public interface OperationPermanenteService {

    OperationPermanenteDtos listerOperationPermanentesParUtilisateur(String email);
    OperationPermanenteDto recupererOperationPermanenteParUtilisateur(String idUtilisateur, String idOperationPermanente);
    OperationPermanenteDto ajouterOperationPermanenteAUtilisateur(
            String email, OperationPermanenteDto operationPermanenteDto);
    OperationPermanenteDto modifierOperationPermanenteUtilisateur(String idOperationPermanente, OperationPermanenteDto operationPermanenteDto);
    void supprimerOperationPermanente(String id);

}
