package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Operation;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class OperationRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OperationRepository operationRepository;

    @Test
    public void sauvegarderOperationAvecUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation = creerOperation(cyril);
        mongoTemplate.save(cyril);

        Mono<Operation> resultat = operationRepository.save(operation);

        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNext(operation)
                .verifyComplete();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void supprimerOperation() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation = creerOperation(cyril);
        mongoTemplate.save(cyril);
        mongoTemplate.save(operation);

        Mono<Void> resultat = operationRepository.delete(operation);

        StepVerifier.create(resultat)
                .expectSubscription()
                .verifyComplete();
        Operation resultatOperation = mongoTemplate.findById(operation.getId(), Operation.class);
        assertThat(resultatOperation).isNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listerOperationsParUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        Operation operation1 = creerOperation(cyril);
        Operation operation2 = creerOperation(cyril);
        operation2.setIntitule("operation2");
        operation2.setDateOperation(LocalDate.now().plusDays(1));
        mongoTemplate.save(cyril);
        mongoTemplate.save(operation1);
        mongoTemplate.save(operation2);

        Flux<Operation> resultat = operationRepository.findAllByUtilisateur_IdOrderByDateOperationDesc(cyril.getId());

        StepVerifier.create(resultat.log())
                .expectSubscription()
                .expectNextMatches(operation -> operation.getIntitule().equals("operation2"))
                .expectNextMatches(operation -> operation.getIntitule().equals("operation"))
                .verifyComplete();
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