package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.exception.OperationNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationsMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Operation;
import com.cmarchive.bank.serviceutilisateur.modele.Operations;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
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
    public OperationsDto listerOperationsParUtilisateur(String utilisateurId) {
        return null;
    }

    @Override
    public OperationDto ajouterOperationAUtilisateur(String utilisateurId, OperationDto operationDto) {
        return null;
    }

    @Override
    public OperationDto modifierOperationUtilisateur(OperationDto operationDto) {
        return null;
    }

    @Override
    public void supprimerOperation(OperationDto operationDto) {

    }

    /*@Override
    public OperationsDto listerOperationsParUtilisateur(String utilisateurId) {
        Operations operations = new Operations()
                .setOperations(operationRepository
                        .findAllByUtilisateur_IdOrderByDateOperationDesc(utilisateurId));

        return operationsMapper.mapVersOperationsDto(operations);
    }

    @Override
    public OperationDto ajouterOperationAUtilisateur(String utilisateurId, OperationDto operationDto) {
        Utilisateur utilisateur = recupererUtilisateurParId(utilisateurId);
        Operation operation = operationMapper.mapVersOperation(operationDto);
        operation.setUtilisateur(utilisateur);

        Operation reponse = operationRepository.save(operation);
        return operationMapper.mapVersOperationDto(reponse);
    }

    @Override
    public OperationDto modifierOperationUtilisateur(OperationDto operationDto) {
        Operation operationBdd = recupererOperationDansBdd(operationDto);

        Operation operation = operationMapper.mapVersOperation(operationDto);
        operation.setUtilisateur(operationBdd.getUtilisateur());

        Operation reponse = operationRepository.save(operation);

        return operationMapper.mapVersOperationDto(reponse);
    }

    private Operation recupererOperationDansBdd(OperationDto operationDto) {
        return operationRepository.findById(operationDto.getId())
                    .orElseThrow(() -> new OperationNonTrouveException("Operation non trouvee"));
    }

    @Override
    public void supprimerOperation(OperationDto operationDto) {
        Operation operation = operationMapper.mapVersOperation(operationDto);

        operationRepository.delete(operation);
    }

    private Utilisateur recupererUtilisateurParId(String utilisateurId) {
        UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateur(utilisateurId);
        return utilisateurMapper.mapVersUtilisateur(utilisateurDto);
    }*/
}
