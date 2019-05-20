package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class UtilisateurRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @MockBean
    private ResourceServerConfiguration resourceServerConfiguration;

    @Test
    public void listerUtilisateur() {
        Utilisateur cyril = new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
        Utilisateur melanie = new Utilisateur()
                .setEmail("melanie.boussat@gmail.com")
                .setNom("Boussat")
                .setPrenom("Melanie");
        testEntityManager.persist(cyril);
        testEntityManager.persist(melanie);
        testEntityManager.flush();

        List<Utilisateur> resultats = utilisateurRepository.findAll();

        assertThat(resultats).isNotEmpty()
                .containsExactly(cyril, melanie);
    }

    @Test
    public void sauvegarderUtilisateur() {
        Utilisateur cyril = creerUtilisateur();

        Utilisateur resultat = utilisateurRepository.save(cyril);

        assertThat(resultat).isNotNull()
                .isEqualTo(cyril);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void modifierUtilisateur() {
        Utilisateur test = new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Test")
                .setPrenom("Test");
        testEntityManager.persist(test);
        testEntityManager.flush();
        Utilisateur cyril = creerUtilisateur()
                .setId(test.getId());

        Utilisateur resultat = utilisateurRepository.save(cyril);

        assertThat(resultat).isNotNull()
                .isEqualToComparingFieldByField(cyril);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void recupererUtilisateurParEmail() {
        Utilisateur cyril  = creerUtilisateur();
        testEntityManager.persist(cyril);
        testEntityManager.flush();

        Optional<Utilisateur> resultat = utilisateurRepository.findByEmail("cyril.marchive@gmail.com");

        assertThat(resultat.get()).isNotNull()
                .isEqualTo(cyril);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void recupererUtilisateurParIdOkta() {
        Utilisateur cyril  = creerUtilisateur();
        testEntityManager.persist(cyril);
        testEntityManager.flush();

        Optional<Utilisateur> resultat = utilisateurRepository.findByIdOkta("1");

        assertThat(resultat.get()).isNotNull()
                .isEqualTo(cyril);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void recupererUtilisateurParId() {
        Utilisateur cyril  = creerUtilisateur();
        testEntityManager.persistAndFlush(cyril);

        Optional<Utilisateur> resultat = utilisateurRepository.findById(cyril.getId());

        assertThat(resultat.get()).isNotNull()
                .isEqualTo(cyril);
    }

    @Test
    public void supprimerUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        testEntityManager.persist(cyril);
        testEntityManager.flush();

        utilisateurRepository.delete(cyril);

        Utilisateur resultat = testEntityManager.find(Utilisateur.class, cyril.getId());
        assertThat(resultat).isNull();
    }

    private Utilisateur creerUtilisateur() {
        return new Utilisateur()
                .setIdOkta("1")
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }
}