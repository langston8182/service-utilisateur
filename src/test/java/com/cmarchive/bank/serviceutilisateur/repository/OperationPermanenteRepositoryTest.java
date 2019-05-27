package com.cmarchive.bank.serviceutilisateur.repository;


import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class OperationPermanenteRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OperationPermanenteRepository operationPermanenteRepository;

    @MockBean
    private ResourceServerConfiguration resourceServerConfiguration;

    @Test
    public void sauvegarderOperationPermanenteAvecUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente = creerOperationPermanente(cyril);
        testEntityManager.persist(cyril);
        testEntityManager.flush();

        OperationPermanente resultat = operationPermanenteRepository.save(operationPermanente);

        assertThat(resultat).isEqualToComparingFieldByField(operationPermanente);
    }

    @Test
    public void supprimerOperationPermanente() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente = creerOperationPermanente(cyril);
        testEntityManager.persist(cyril);
        testEntityManager.persist(operationPermanente);
        testEntityManager.flush();

        operationPermanenteRepository.delete(operationPermanente);

        OperationPermanente resultat = testEntityManager.find(OperationPermanente.class, operationPermanente.getId());
        assertThat(resultat).isNull();
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void recupererOperationPermanenteParUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente = creerOperationPermanente(cyril);
        testEntityManager.persist(cyril);
        testEntityManager.persist(operationPermanente);
        testEntityManager.flush();

        OperationPermanente resultat = operationPermanenteRepository.findByUtilisateur_IdAndId(cyril.getId(), operationPermanente.getId());

        assertThat(resultat).isNotNull();
        assertThat(resultat.getIntitule()).isEqualTo("intitule");
        assertThat(resultat.getUtilisateur().getNom()).isEqualTo("Marchive");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listerOperationPermanenteParUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente1 = creerOperationPermanente(cyril);
        OperationPermanente operationPermanente2 = creerOperationPermanente(cyril);
        operationPermanente2.setJour(2);
        testEntityManager.persist(cyril);
        testEntityManager.persist(operationPermanente1);
        testEntityManager.persist(operationPermanente2);
        testEntityManager.flush();

        List<OperationPermanente> resultat = operationPermanenteRepository.findAllByUtilisateur_Id(cyril.getId());

        assertThat(resultat).hasSize(2);
        assertThat(resultat.get(1).getJour()).isEqualTo(2);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listerOperationPermanenteParEmailUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente1 = creerOperationPermanente(cyril);
        OperationPermanente operationPermanente2 = creerOperationPermanente(cyril);
        operationPermanente2.setJour(2);
        testEntityManager.persist(cyril);
        testEntityManager.persist(operationPermanente1);
        testEntityManager.persist(operationPermanente2);
        testEntityManager.flush();

        List<OperationPermanente> resultat = operationPermanenteRepository.findAllByUtilisateur_Email(cyril.getEmail());

        assertThat(resultat).hasSize(2);
        assertThat(resultat.get(1).getJour()).isEqualTo(2);
    }

    private OperationPermanente creerOperationPermanente(Utilisateur cyril) {
        OperationPermanente operationPermanente = new OperationPermanente()
                .setIntitule("intitule")
                .setJour(12)
                .setPrix(BigDecimal.TEN)
                .setUtilisateur(cyril);

        return operationPermanente;
    }

    private Utilisateur creerUtilisateur() {
        return new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }

}