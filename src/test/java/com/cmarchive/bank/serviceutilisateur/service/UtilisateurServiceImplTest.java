package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateurServiceImplTest {

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Test
    public void listerUtilisateurs() {
        Utilisateur cyril = new Utilisateur();
        Utilisateur melanie = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        UtilisateurDto melanieDto = new UtilisateurDto();
        given(utilisateurRepository.findAll()).willReturn(Flux.just(cyril, melanie));
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);
        given(utilisateurMapper.mapVersUtilisateurDto(melanie)).willReturn(melanieDto);

        Flux<UtilisateurDto> resultat = utilisateurService.listerUtilisateurs();

        then(utilisateurRepository).should().findAll();
        StepVerifier.create(resultat.log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void recupererUtilisateur_nominal() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurRepository.findById(anyString())).willReturn(Mono.just(cyril));
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);

        Mono<UtilisateurDto> resultat = utilisateurService.recupererUtilisateur(anyString());

        then(utilisateurRepository).should().findById(anyString());
        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNext(cyrilDto)
                .verifyComplete();
    }

    /*@Test
    public void recupererUtilisateurInexistant() {
        given(utilisateurRepository.findById(anyString())).willThrow(UtilisateurNonTrouveException.class);

        Throwable thrown = catchThrowable(() -> utilisateurService.recupererUtilisateur(anyString()));

        then(utilisateurRepository).should().findById(anyString());
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurNonTrouveException.class);
    }*/

    @Test
    public void sauvegarderUtilisateur() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        Utilisateur reponse = new Utilisateur()
                .setId("1");
        given(utilisateurRepository.save(cyril)).willReturn(Mono.just(reponse));
        given(utilisateurMapper.mapVersUtilisateurDto(reponse)).willReturn(cyrilDto);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);

        Mono<UtilisateurDto> resultat = utilisateurService.creerUtilisateur(cyrilDto);

        then(utilisateurRepository).should().save(cyril);
        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNext(cyrilDto)
                .verifyComplete();
    }

    /*@Test
    public void sauvegarderUtilisateur_DejaExistant() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurRepository.save(cyril)).willThrow(DataIntegrityViolationException.class);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);

        Throwable thrown = catchThrowable(() -> utilisateurService.creerUtilisateur(cyrilDto));

        then(utilisateurMapper).should(never()).mapVersUtilisateurDto(any(Utilisateur.class));
        assertThat(thrown).isNotNull();
        assertThat(thrown).isExactlyInstanceOf(UtilisateurDejaPresentException.class);
    }*/

    @Test
    public void modifierUtilisateur() {
        String id = "1";
        Utilisateur cyrilRecuperedeBdd = new Utilisateur()
                .setId(id)
                .setMotDePasse("motDePasse");
        UtilisateurDto cyrilDtoRecupereDeBdd = new UtilisateurDto()
                .setId(id)
                .setMotDePasse("motDePasse");
        Utilisateur cyril = spy(Utilisateur.class);
        cyril.setId(id);
        UtilisateurDto cyrilDto = new UtilisateurDto()
                .setId(id)
                .setMotDePasse("motDePasseModifie");
        given(utilisateurRepository.findById(id)).willReturn(Mono.just(cyrilRecuperedeBdd));
        given(utilisateurMapper.mapVersUtilisateurDto(cyrilRecuperedeBdd)).willReturn(cyrilDtoRecupereDeBdd);
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);
        given(utilisateurRepository.save(cyril)).willReturn(Mono.just(cyril));
        given(utilisateurMapper.mapVersUtilisateurDto(cyril)).willReturn(cyrilDto);

        Mono<UtilisateurDto> resultat = utilisateurService.modifierUtilisateur(cyrilDto);

        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNext(cyrilDto)
                .verifyComplete();
        then(utilisateurRepository).should().save(cyril);
        then(utilisateurRepository).should().findById(id);
        then(cyril).should().setMotDePasse("motDePasse");
    }

    @Test
    public void supprimerUtilisateur() {
        Utilisateur cyril = new Utilisateur();
        UtilisateurDto cyrilDto = new UtilisateurDto();
        given(utilisateurMapper.mapVersUtilisateur(cyrilDto)).willReturn(cyril);
        given(utilisateurRepository.delete(cyril)).willReturn(Mono.empty());

        Mono<Void> resultat = utilisateurService.supprimerUtilisateur(cyrilDto);

        StepVerifier.create(resultat.log())
                .expectSubscription()
                .verifyComplete();
        then(utilisateurRepository).should().delete(cyril);
    }
}