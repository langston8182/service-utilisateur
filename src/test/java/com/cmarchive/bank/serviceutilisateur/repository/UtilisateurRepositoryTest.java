package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    public void recupererUtilisateur() {
        Utilisateur cyril  = creerUtilisateur();
        testEntityManager.persist(cyril);
        testEntityManager.flush();

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
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }
}