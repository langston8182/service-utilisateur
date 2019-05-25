package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.OperationDto;
import com.cmarchive.bank.ressource.model.OperationDtos;

/**
 * Service pour les operations utilisateurs.
 */
public interface OperationService {

    OperationDtos listerOperationsParUtilisateur(String idUtilisateur);
    OperationDto recupererOperationParUtilisateur(String idUtilisateur, String idOperation);
    OperationDto ajouterOperationAUtilisateur(String email, OperationDto operationDto);
    OperationDto modifierOperationUtilisateur(OperationDto operationDto);
    void supprimerOperation(OperationDto operationDto);

}
