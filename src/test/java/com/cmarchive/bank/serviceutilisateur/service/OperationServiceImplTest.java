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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

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
                .findAllByUtilisateur_IdOktaOrderByDateOperationDesc(id))
                .willReturn(Stream.of(operation1, operation2).collect(Collectors.toList()));
        given(operationsMapper.mapVersOperationsDto(any(Operations.class))).willReturn(operationsDto);

        OperationsDto resultat = operationService.listerOperationsParUtilisateur(id);

        then(operationRepository).should().findAllByUtilisateur_IdOktaOrderByDateOperationDesc(id);
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
        given(utilisateurService.recupererUtilisateurParIdOkta("1")).willReturn(utilisateurDto);
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
    public void modifierOperationUtilisateur() {
        String id = "1";
        OperationDto operationDto = new OperationDto().setId(id);
        String email = "email";
        Operation operationBdd = new Operation()
                .setUtilisateur(new Utilisateur().setEmail(email));
        Operation operation = new Operation();
        Operation operationReponse = new Operation()
                .setUtilisateur(new Utilisateur().setEmail(email));
        OperationDto operationDtoReponse = new OperationDto()
                .setUtilisateurDto(new UtilisateurDto().setEmail(email));
        given(operationRepository.findById(id)).willReturn(Optional.of(operationBdd));
        given(operationMapper.mapVersOperation(operationDto)).willReturn(operation);
        given(operationRepository.save(operation)).willReturn(operationReponse);
        given(operationMapper.mapVersOperationDto(operationReponse)).willReturn(operationDtoReponse);

        OperationDto resultat = operationService.modifierOperationUtilisateur(operationDto);

        then(operationRepository).should().findById(id);
        then(operationRepository).should().save(operation);
        assertThat(resultat).isNotNull();
        assertThat(resultat.getUtilisateurDto().getEmail()).isEqualTo(email);
    }

    @Test
    public void modifierOperationUtilisateur_OperationNonTrouvee() {
        String id = "1";
        OperationDto operationDto = new OperationDto().setId(id);
        given(operationRepository.findById(id)).willThrow(OperationNonTrouveException.class);

        Throwable thrown = catchThrowable(() -> operationService.modifierOperationUtilisateur(operationDto));

        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(OperationNonTrouveException.class);
        verifyZeroInteractions(operationMapper);
        then(operationRepository).should().findById(id);
        verifyNoMoreInteractions(operationRepository);
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