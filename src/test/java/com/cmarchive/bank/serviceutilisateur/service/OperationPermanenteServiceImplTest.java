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
        OperationPermanenteDtos operationPermanentesDto = new OperationPermanenteDtos();
        operationPermanentesDto.setOperationPermanenteDtos(
                Stream.of(operationPermanenteDto1, operationPermanenteDto2).collect(Collectors.toList()));
        String idUtilisateur = "1";
        given(operationPermanenteRepository
                .findAllByUtilisateur_Id(idUtilisateur))
                .willReturn(Stream.of(operationPermanente1, operationPermanente2).collect(Collectors.toList()));
        given(operationPermanentesMapper
                .mapVersOperationPermanenteDtos(any(OperationPermanentes.class)))
                .willReturn(operationPermanentesDto);

        OperationPermanenteDtos resultat = operationPermanenteService.listerOperationPermanentesParUtilisateur(idUtilisateur);

        then(operationPermanenteRepository).should().findAllByUtilisateur_Id(idUtilisateur);
        assertThat(resultat.getOperationPermanenteDtos()).isNotEmpty()
                .containsExactly(operationPermanenteDto1, operationPermanenteDto2);
    }

    @Test
    public void recupererOperationPermanenteParUtilisateur() {
        UtilisateurDto utilisateurDto = new UtilisateurDto();
        OperationPermanente operationPermanente = new OperationPermanente();
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto();
        given(utilisateurService.recupererUtilisateur("1")).willReturn(utilisateurDto);
        given(operationPermanenteRepository.findByUtilisateur_IdAndId("1", "2")).willReturn(operationPermanente);
        given(operationPermanenteMapper.mapVersOperationPermanenteDto(operationPermanente)).willReturn(operationPermanenteDto);

        OperationPermanenteDto resultat = operationPermanenteService.recupererOperationPermanenteParUtilisateur("1", "2");

        then(operationPermanenteRepository).should().findByUtilisateur_IdAndId("1", "2");
        assertThat(resultat).isNotNull()
                .isEqualTo(operationPermanenteDto);
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
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto()
                .identifiant(id);
        String email = "email";
        OperationPermanente operationPermanenteBdd = new OperationPermanente()
                .setUtilisateur(new Utilisateur().setEmail(email));
        OperationPermanente operationPermanente = new OperationPermanente();
        OperationPermanente operationPermanenteReponse = new OperationPermanente()
                .setUtilisateur(new Utilisateur().setEmail(email));
        OperationPermanenteDto operationPermanenteDtoReponse = new OperationPermanenteDto()
                .utilisateurDto(new UtilisateurDto().email(email));
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
        OperationPermanenteDto operationPermanenteDto = new OperationPermanenteDto()
                .identifiant(id);
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
        operationPermanenteService.supprimerOperationPermanente("1");

        then(operationPermanenteRepository).should().deleteById("1");
    }
}