package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.ressource.model.UtilisateurDtos;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceImplTest {

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateursMapper utilisateursMapper;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Test
    public void listerUtilisateurs() {
        Utilisateur cyril = new Utilisateur();
        Utilisateur melanie = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        UtilisateurDto melanieDto = new UtilisateurDto();
        UtilisateurDtos utilisateursDto = new UtilisateurDtos();
        utilisateursDto.utilisateurDtos(Stream.of(cyrilDto, melanieDto).collect(Collectors.toList()));
        given(utilisateurRepository.findAll()).willReturn(Stream.of(cyril, melanie).collect(Collectors.toList()));
        given(utilisateursMapper.mapVersUtilisateurDtos(any(Utilisateurs.class))).willReturn(utilisateursDto);

        UtilisateurDtos resultat = utilisateurService.listerUtilisateurs();

        then(utilisateurRepository).should().findAll();
        assertThat(resultat.getUtilisateurDtos()).isNotEmpty()
                .containsExactly(cyrilDto, melanieDto);
    }

    @Test
    public void recupererUtilisateurParEmail_nominal() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurRepository.findByEmail(anyString())).willReturn(Optional.of(cyril));
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);

        UtilisateurDto resultat = utilisateurService.recupererUtilisateurParEmail(anyString());

        then(utilisateurRepository).should().findByEmail(anyString());
        assertThat(resultat).isEqualTo(cyrilDto);
    }

    @Test
    public void recupererUtilisateur_nominal() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurRepository.findById(anyString())).willReturn(Optional.of(cyril));
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);

        UtilisateurDto resultat = utilisateurService.recupererUtilisateur(anyString());

        then(utilisateurRepository).should().findById(anyString());
        assertThat(resultat).isEqualTo(cyrilDto);
    }

    @Test
    public void recupererUtilisateurInexistant() {
        given(utilisateurRepository.findById(anyString())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> utilisateurService.recupererUtilisateur(anyString()));

        then(utilisateurRepository).should().findById(anyString());
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurNonTrouveException.class);
    }

    @Test
    public void recupererUtilisateurParEmail_UtilisateurInexistant() {
        given(utilisateurRepository.findByEmail(anyString())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> utilisateurService.recupererUtilisateurParEmail(anyString()));

        then(utilisateurRepository).should().findByEmail(anyString());
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurNonTrouveException.class);
    }

    @Test
    public void sauvegarderUtilisateur() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        Utilisateur reponse = new Utilisateur()
                .setId("1");
        given(utilisateurRepository.save(cyril)).willReturn(reponse);
        given(utilisateurMapper.mapVersUtilisateurDto(reponse)).willReturn(cyrilDto);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);

        UtilisateurDto resultat = utilisateurService.creerUtilisateur(cyrilDto);

        then(utilisateurRepository).should().save(cyril);
        assertThat(resultat).isNotNull()
                .isEqualTo(cyrilDto);
    }

    @Test
    public void sauvegarderUtilisateur_DejaExistant() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurRepository.save(cyril)).willThrow(DataIntegrityViolationException.class);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);

        Throwable thrown = catchThrowable(() -> utilisateurService.creerUtilisateur(cyrilDto));

        then(utilisateurMapper).should(never()).mapVersUtilisateurDto(any(Utilisateur.class));
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurDejaPresentException.class);
    }

    @Test
    public void modifierUtilisateur() {
        String id = "1";
        String email = "cyril.marchive@gmail.com";
        Utilisateur cyrilRecuperedeBdd = new Utilisateur()
                .setId(id)
                .setEmail(email);
        UtilisateurDto cyrilDtoRecupereDeBdd = new UtilisateurDto()
                .identifiant(id)
                .email(email);
        Utilisateur cyril = spy(Utilisateur.class);
        cyril.setId(id);
        cyril.setEmail(email);
        UtilisateurDto cyrilDto = new UtilisateurDto()
                .identifiant(id)
                .email(email);
        given(utilisateurRepository.findByEmail(email)).willReturn(Optional.of(cyrilRecuperedeBdd));
        given(utilisateurMapper.mapVersUtilisateurDto(cyrilRecuperedeBdd)).willReturn(cyrilDtoRecupereDeBdd);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);
        given(utilisateurRepository.save(cyril)).willReturn(cyril);
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);

        UtilisateurDto resultat = utilisateurService.modifierUtilisateur(cyrilDto);

        then(utilisateurRepository).should().save(cyril);
        then(utilisateurRepository).should().findByEmail(email);
        assertThat(resultat).isNotNull()
                .isEqualTo(cyrilDto);
    }

    @Test
    public void modifierUtilisateur_UtilisateurInexistant() {
        String email = "cyril.marchive@gmail.com";
        UtilisateurDto cyril = new UtilisateurDto()
                .email(email);
        given(utilisateurRepository.findByEmail(email)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> utilisateurService.modifierUtilisateur(cyril));

        then(utilisateurRepository).should().findByEmail(email);
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurNonTrouveException.class);
    }

    @Test
    public void supprimerUtilisateur() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);

        utilisateurService.supprimerUtilisateur(cyrilDto);

        then(utilisateurRepository).should().delete(cyril);
    }
}