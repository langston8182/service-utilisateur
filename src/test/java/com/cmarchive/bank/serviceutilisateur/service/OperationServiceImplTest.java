package com.cmarchive.bank.serviceutilisateur.service;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceImplTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private OperationMapper operationMapper;

    @Mock
    private OperationsMapper operationsMapper;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Test
    public void listerOperationsParUtilisateur() {
        Operation operation1 = new Operation();
        Operation operation2 = new Operation();
        OperationDto operationDto1 = new OperationDto();
        OperationDto operationDto2 = new OperationDto();
        OperationsDto operationsDto = new OperationsDto();
        operationsDto.setOperationDtos(Stream.of(operationDto1, operationDto2).collect(Collectors.toList()));
        String id = "1";
        given(operationRepository
                .findAllByUtilisateur_IdOrderByDateOperationDesc(id))
                .willReturn(Stream.of(operation1, operation2).collect(Collectors.toList()));
        given(operationsMapper.mapVersOperationsDto(any(Operations.class))).willReturn(operationsDto);

        OperationsDto resultat = operationService.listerOperationsParUtilisateur(id);

        then(operationRepository).should().findAllByUtilisateur_IdOrderByDateOperationDesc(id);
        assertThat(resultat.getOperationDtos()).isNotEmpty()
                .containsExactly(operationDto1, operationDto2);
    }

    @Test
    public void ajouterOperationAUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurDto utilisateurDto = new UtilisateurDto();
        Operation operation = new Operation();
        OperationDto operationDto = new OperationDto();
        Operation reponse = new Operation()
                .setUtilisateur(utilisateur);
        given(utilisateurService.recupererUtilisateur("1")).willReturn(utilisateurDto);
        given(utilisateurMapper.mapVersUtilisateur(utilisateurDto)).willReturn(utilisateur);
        given(operationRepository.save(operation)).willReturn(reponse);
        given(operationMapper.mapVersOperationDto(reponse)).willReturn(operationDto);
        given(operationMapper.mapVersOperation(operationDto)).willReturn(operation);

        OperationDto resultat = operationService.ajouterOperationAUtilisateur("1", operationDto);

        then(operationRepository).should().save(operation);
        assertThat(resultat).isNotNull()
                .isEqualTo(operationDto);
    }

    @Test
    public void supprimerOperation() {
        Operation operation = new Operation();
        OperationDto operationDto = new OperationDto();
        given(operationMapper.mapVersOperation(operationDto)).willReturn(operation);

        operationService.supprimerOperation(operationDto);

        then(operationRepository).should().delete(operation);
    }
}