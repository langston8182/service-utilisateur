package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.exception.OperationNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationPermanenteMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.OperationPermanentesMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanentes;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanenteDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationPermanentesDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.repository.OperationPermanenteRepository;
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
public class OperationPermanenteServiceImplTest {

    @InjectMocks
    private OperationPermanenteServiceImpl operationPermanenteService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private OperationPermanenteRepository operationPermanenteRepository;

    @Mock
    private OperationPermanenteMapper operationPermanenteMapper;

    @Mock
    private OperationPermanentesMapper operationPermanentesMapper;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Test
    public void listerOperationPermanentesParUtilisateur() {
        OperationPermanente operationPermanente1 = new OperationPermanente();
        OperationPermanente operationPermanente2 = new OperationPermanente();
        OperationPermanenteDto operationPermanenteDto1 = new OperationPermanenteDto();
        OperationPermanenteDto operationPermanenteDto2 = new OperationPermanenteDto();
        OperationPermanentesDto operationPermanentesDto = new OperationPermanentesDto();
        operationPermanentesDto.setOperationPermanenteDtos(
                Stream.of(operationPermanenteDto1, operationPermanenteDto2).collect(Collectors.toList()));
        String email = "cyril.marchive@gmail.com";
        given(operationPermanenteRepository
                .findAllByUtilisateur_Email(email))
                .willReturn(Stream.of(operationPermanente1, operationPermanente2).collect(Collectors.toList()));
        given(operationPermanentesMapper
                .mapVersOperationPermanentesDto(any(OperationPermanentes.class)))
                .willReturn(operationPermanentesDto);

        OperationPermanentesDto resultat = operationPermanenteService.listerOperationPermanentesParUtilisateur(email);

        then(operationPermanenteRepository).should().findAllByUtilisateur_Email(email);
        assertThat(resultat.getOperationPermanenteDtos()).isNotEmpty()
                .containsExactly(operationPermanenteDto1, operationPermanenteDto2);
    }

    @Test
    public void ajouterOperationPermanenteAUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurDto utilisateurDto = new UtilisateurDto();
        OperationPermanente operationPermanente = new OperationPermanente();
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto();
        OperationPermanente reponse = new OperationPermanente()
                .setUtilisateur(utilisateur);
        given(utilisateurService.recupererUtilisateurParEmail("cyril.marchive@gmail.com")).willReturn(utilisateurDto);
        given(utilisateurMapper.mapVersUtilisateur(utilisateurDto)).willReturn(utilisateur);
        given(operationPermanenteRepository.save(operationPermanente)).willReturn(reponse);
        given(operationPermanenteMapper.mapVersOperationPermanenteDto(reponse)).willReturn(operationPermanenteDto);
        given(operationPermanenteMapper.mapVersOperationPermanente(operationPermanenteDto))
                .willReturn(operationPermanente);

        OperationPermanenteDto resultat = operationPermanenteService.ajouterOperationPermanenteAUtilisateur(
                "cyril.marchive@gmail.com", operationPermanenteDto);

        then(operationPermanenteRepository).should().save(operationPermanente);
        assertThat(resultat).isNotNull()
                .isEqualTo(operationPermanenteDto);
    }

    @Test
    public void modifierOperationPermanenteUtilisateur() {
        String id = "1";
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto().setId(id);
        String email = "email";
        OperationPermanente operationPermanenteBdd = new OperationPermanente()
                .setUtilisateur(new Utilisateur().setEmail(email));
        OperationPermanente operationPermanente = new OperationPermanente();
        OperationPermanente operationPermanenteReponse = new OperationPermanente()
                .setUtilisateur(new Utilisateur().setEmail(email));
        OperationPermanenteDto operationPermanenteDtoReponse = new OperationPermanenteDto()
                .setUtilisateurDto(new UtilisateurDto().setEmail(email));
        given(operationPermanenteRepository.findById(id)).willReturn(Optional.of(operationPermanenteBdd));
        given(operationPermanenteMapper.mapVersOperationPermanente(operationPermanenteDto))
                .willReturn(operationPermanente);
        given(operationPermanenteRepository.save(operationPermanente)).willReturn(operationPermanenteReponse);
        given(operationPermanenteMapper.mapVersOperationPermanenteDto(operationPermanenteReponse))
                .willReturn(operationPermanenteDtoReponse);

        OperationPermanenteDto resultat = operationPermanenteService.modifierOperationPermanenteUtilisateur(
                operationPermanenteDto);

        then(operationPermanenteRepository).should().findById(id);
        then(operationPermanenteRepository).should().save(operationPermanente);
        assertThat(resultat).isNotNull();
        assertThat(resultat.getUtilisateurDto().getEmail()).isEqualTo(email);
    }

    @Test
    public void modifierOperationPermanenteUtilisateur_OperationPermanenteNonTrouvee() {
        String id = "1";
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto().setId(id);
        given(operationPermanenteRepository.findById(id)).willThrow(OperationNonTrouveException.class);

        Throwable thrown = catchThrowable(() -> operationPermanenteService
                .modifierOperationPermanenteUtilisateur(operationPermanenteDto));

        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(OperationNonTrouveException.class);
        verifyZeroInteractions(operationPermanenteMapper);
        then(operationPermanenteRepository).should().findById(id);
        verifyNoMoreInteractions(operationPermanenteRepository);
    }

    @Test
    public void supprimerOperationPermanente() {
        OperationPermanente operationPermanente = new OperationPermanente();
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto();
        given(operationPermanenteMapper.mapVersOperationPermanente(operationPermanenteDto))
                .willReturn(operationPermanente);

        operationPermanenteService.supprimerOperationPermanente(operationPermanenteDto);

        then(operationPermanenteRepository).should().delete(operationPermanente);
    }
}