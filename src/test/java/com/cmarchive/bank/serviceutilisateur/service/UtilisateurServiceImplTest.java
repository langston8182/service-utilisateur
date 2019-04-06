package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceImplTest {

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Test
    public void listerUtilisateurs() {
        Utilisateur cyril = new Utilisateur();
        Utilisateur melanie = new Utilisateur();
        List<Utilisateur> utilisateurs = Stream.of(cyril, melanie).collect(Collectors.toList());
        given(utilisateurRepository.findAll()).willReturn(utilisateurs);

        Utilisateurs resultat = utilisateurService.listerUtilisateurs();

        then(utilisateurRepository).should().findAll();
        assertThat(resultat.getUtilisateurs()).isNotEmpty()
                .containsExactly(cyril, melanie);
    }

    @Test
    public void recupererUtilisateur_nominal() {
        Utilisateur cyril = new Utilisateur();
        given(utilisateurRepository.findById(anyString())).willReturn(Optional.of(cyril));

        Utilisateur resultat = utilisateurService.recupererUtilisateur(anyString());

        then(utilisateurRepository).should().findById(anyString());
        assertThat(resultat).isEqualTo(cyril);
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
        Utilisateur reponse = new Utilisateur()
                .setId("1");
        given(utilisateurRepository.save(cyril)).willReturn(reponse);

        Utilisateur resultat = utilisateurService.sauvegarderUtilisateur(cyril);

        then(utilisateurRepository).should().save(cyril);
        assertThat(resultat).isNotNull()
                .isEqualTo(reponse);
    }

    @Test
    public void supprimerUtilisateur() {
        Utilisateur cyril = new Utilisateur();

        utilisateurService.supprimerUtilisateur(cyril);

        then(utilisateurRepository).should().delete(cyril);
    }
}