package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;

/**
 * Service pour les operations utilisateurs.
 */
public interface OperationService {

    OperationsDto listerOperationsParUtilisateur(String email);
    OperationDto ajouterOperationAUtilisateur(String email, OperationDto operationDto);
    OperationDto modifierOperationUtilisateur(OperationDto operationDto);
    void supprimerOperation(OperationDto operationDto);

}
