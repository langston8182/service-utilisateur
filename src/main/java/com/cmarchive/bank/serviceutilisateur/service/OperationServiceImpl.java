package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.OperationDto;
import com.cmarchive.bank.ressource.model.OperationDtos;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.exception.OperationNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationsMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Operation;
import com.cmarchive.bank.serviceutilisateur.modele.Operations;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.repository.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationServiceImpl implements OperationService {

    private UtilisateurService utilisateurService;
    private OperationRepository operationRepository;
    private UtilisateurMapper utilisateurMapper;
    private OperationMapper operationMapper;
    private OperationsMapper operationsMapper;

    public OperationServiceImpl(UtilisateurService utilisateurService,
                                OperationRepository operationRepository,
                                UtilisateurMapper utilisateurMapper,
                                OperationMapper operationMapper,
                                OperationsMapper operationsMapper) {
        this.utilisateurService = utilisateurService;
        this.operationRepository = operationRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.operationMapper = operationMapper;
        this.operationsMapper = operationsMapper;
    }

    @Override
    public OperationDtos listerOperationsParUtilisateur(String idUtilisateur) {
        Operations operations = new Operations()
                .setOperations(operationRepository
                        .findAllByUtilisateur_IdOrderByDateOperationDesc(idUtilisateur));

        return operationsMapper.mapVersOperationDtos(operations);
    }

    @Override
    public OperationDto recupererOperationParUtilisateur(String idUtilisateur, String idOperation) {
        recupererUtilisateurParId(idUtilisateur);
        Operation operation = operationRepository.findByUtilisateur_IdAndId(idUtilisateur, idOperation);

        return operationMapper.mapVersOperationDto(operation);
    }

    @Override
    public OperationDto ajouterOperationAUtilisateur(String email, OperationDto operationDto) {
        Utilisateur utilisateur = recupererUtilisateurParEmail(email);
        Operation operation = operationMapper.mapVersOperation(operationDto);
        operation.setUtilisateur(utilisateur);

        Operation reponse = operationRepository.save(operation);
        return operationMapper.mapVersOperationDto(reponse);
    }

    @Override
    public OperationDto modifierOperationUtilisateur(String idOperation, OperationDto operationDto) {
        Operation operationBdd = recupererOperationDansBdd(idOperation);

        Operation operation = operationMapper.mapVersOperation(operationDto);
        operation.setUtilisateur(operationBdd.getUtilisateur());

        Operation reponse = operationRepository.save(operation);

        return operationMapper.mapVersOperationDto(reponse);
    }

    private Operation recupererOperationDansBdd(String id) {
        return operationRepository.findById(id)
                    .orElseThrow(() -> new OperationNonTrouveException("Operation non trouvee"));
    }

    @Override
    public void supprimerOperation(String id) {
        operationRepository.deleteById(id);
    }

    private Utilisateur recupererUtilisateurParEmail(String email) {
        UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateurParEmail(email);
        return utilisateurMapper.mapVersUtilisateur(utilisateurDto);
    }

    private Utilisateur recupererUtilisateurParId(String id) {
        UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateur(id);
        return utilisateurMapper.mapVersUtilisateur(utilisateurDto);
    }
}
