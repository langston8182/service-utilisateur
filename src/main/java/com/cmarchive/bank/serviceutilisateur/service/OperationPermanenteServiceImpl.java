package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.OperationPermanenteDto;
import com.cmarchive.bank.ressource.model.OperationPermanenteDtos;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.exception.OperationNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationPermanenteMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationPermanentesMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanentes;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.repository.OperationPermanenteRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationPermanenteServiceImpl implements OperationPermanenteService {

    private UtilisateurService utilisateurService;
    private OperationPermanenteRepository operationPermanenteRepository;
    private OperationPermanenteMapper operationPermanenteMapper;
    private OperationPermanentesMapper operationPermanentesMapper;
    private UtilisateurMapper utilisateurMapper;

    public OperationPermanenteServiceImpl(UtilisateurService utilisateurService,
                                          OperationPermanenteRepository operationPermanenteRepository,
                                          OperationPermanenteMapper operationPermanenteMapper,
                                          OperationPermanentesMapper operationPermanentesMapper,
                                          UtilisateurMapper utilisateurMapper) {
        this.utilisateurService = utilisateurService;
        this.operationPermanenteRepository = operationPermanenteRepository;
        this.operationPermanenteMapper = operationPermanenteMapper;
        this.operationPermanentesMapper = operationPermanentesMapper;
        this.utilisateurMapper = utilisateurMapper;
    }



    @Override
    public OperationPermanenteDtos listerOperationPermanentesParUtilisateur(String idUtilisateur) {
        OperationPermanentes operationPermanentes = new OperationPermanentes()
                .setOperationPermanentes(operationPermanenteRepository
                        .findAllByUtilisateur_Id(idUtilisateur));

        return operationPermanentesMapper.mapVersOperationPermanenteDtos(operationPermanentes);
    }

    @Override
    public OperationPermanenteDto recupererOperationPermanenteParUtilisateur(String idUtilisateur, String idOperationPermanente) {
        recupererUtilisateur(idUtilisateur);
        OperationPermanente operationPermanente = operationPermanenteRepository.findByUtilisateur_IdAndId(idUtilisateur, idOperationPermanente);

        return operationPermanenteMapper.mapVersOperationPermanenteDto(operationPermanente);
    }

    @Override
    public OperationPermanenteDto ajouterOperationPermanenteAUtilisateur(String email, OperationPermanenteDto operationPermanenteDto) {
        Utilisateur utilisateur = recupererUtilisateurParEmail(email);
        OperationPermanente operationPermanente = operationPermanenteMapper
                .mapVersOperationPermanente(operationPermanenteDto);
        operationPermanente.setUtilisateur(utilisateur);

        OperationPermanente reponse = operationPermanenteRepository.save(operationPermanente);
        return operationPermanenteMapper.mapVersOperationPermanenteDto(reponse);
    }

    @Override
    public OperationPermanenteDto modifierOperationPermanenteUtilisateur(String idOperationPermanente, OperationPermanenteDto operationPermanenteDto) {
        OperationPermanente operationPermanenteBdd = recupererOperationPermanenteDansBdd(idOperationPermanente);

        OperationPermanente operationPermanente =
                operationPermanenteMapper.mapVersOperationPermanente(operationPermanenteDto);
        operationPermanente.setUtilisateur(operationPermanenteBdd.getUtilisateur());

        OperationPermanente reponse = operationPermanenteRepository.save(operationPermanente);

        return operationPermanenteMapper.mapVersOperationPermanenteDto(reponse);
    }

    @Override
    public void supprimerOperationPermanente(String id) {
        operationPermanenteRepository.deleteById(id);
    }

    private Utilisateur recupererUtilisateurParEmail(String email) {
        UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateurParEmail(email);
        return utilisateurMapper.mapVersUtilisateur(utilisateurDto);
    }

    private Utilisateur recupererUtilisateur(String id) {
        UtilisateurDto utilisateurDto = utilisateurService.recupererUtilisateur(id);
        return utilisateurMapper.mapVersUtilisateur(utilisateurDto);
    }

    private OperationPermanente recupererOperationPermanenteDansBdd(String id) {
        return operationPermanenteRepository.findById(id)
                .orElseThrow(() -> new OperationNonTrouveException("Operation permanente non trouvee"));
    }
}
