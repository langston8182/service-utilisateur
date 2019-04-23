package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Operation;
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
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class OperationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OperationRepository operationRepository;

    @MockBean
    private ResourceServerConfiguration resourceServerConfiguration;

    @Test
    public void sauvegarderOperationAvecUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation = creerOperation(cyril);
        testEntityManager.persist(cyril);
        testEntityManager.flush();

        Operation resultat = operationRepository.save(operation);

        assertThat(resultat).isEqualToComparingOnlyGivenFields(operation);
    }

    @Test
    public void supprimerOperation() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation = creerOperation(cyril);
        testEntityManager.persist(cyril);
        testEntityManager.persist(operation);
        testEntityManager.flush();

        operationRepository.delete(operation);

        Operation resultat = testEntityManager.find(Operation.class, operation.getId());
        assertThat(resultat).isNull();
    }

    @Test
    public void listerOperationsParUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation1 = creerOperation(cyril);
        Operation operation2 = creerOperation(cyril);
        operation2.setDateOperation(LocalDate.now().plusDays(1));
        testEntityManager.persist(cyril);
        testEntityManager.persist(operation1);
        testEntityManager.persist(operation2);
        testEntityManager.flush();

        List<Operation> resultat = operationRepository.findAllByUtilisateur_IdOrderByDateOperationDesc(cyril.getId());

        assertThat(resultat).hasSize(2);
        assertThat(resultat.get(0).getDateOperation()).isAfter(resultat.get(1).getDateOperation());
    }

    private Operation creerOperation(Utilisateur cyril) {
        return new Operation()
                    .setDateOperation(LocalDate.now())
                    .setIntitule("operation")
                    .setPrix(BigDecimal.TEN)
                    .setUtilisateur(cyril);
    }

    private Utilisateur creerUtilisateur() {
        return new Utilisateur()
                    .setEmail("cyril.marchive@gmail.com")
                    .setNom("Marchive")
                    .setPrenom("Cyril");
    }
}