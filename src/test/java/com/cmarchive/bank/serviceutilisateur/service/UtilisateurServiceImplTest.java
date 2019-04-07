package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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
        UtilisateursDto utilisateursDto = new UtilisateursDto();
        utilisateursDto.setUtilisateursDtos(Stream.of(cyrilDto, melanieDto).collect(Collectors.toList()));
        given(utilisateurRepository.findAll()).willReturn(Stream.of(cyril, melanie).collect(Collectors.toList()));
        given(utilisateursMapper.mapVersUtilisateursDto(any(Utilisateurs.class))).willReturn(utilisateursDto);

        UtilisateursDto resultat = utilisateurService.listerUtilisateurs();

        then(utilisateurRepository).should().findAll();
        assertThat(resultat.getUtilisateursDtos()).isNotEmpty()
                .containsExactly(cyrilDto, melanieDto);
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
        given(utilisateurRepository.findById(anyString())).willThrow(UtilisateurNonTrouveException.class);

        Throwable thrown = catchThrowable(() -> utilisateurService.recupererUtilisateur(anyString()));

        then(utilisateurRepository).should().findById(anyString());
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

        UtilisateurDto resultat = utilisateurService.sauvegarderUtilisateur(cyrilDto);

        then(utilisateurRepository).should().save(cyril);
        assertThat(resultat).isNotNull()
                .isEqualTo(cyrilDto);
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