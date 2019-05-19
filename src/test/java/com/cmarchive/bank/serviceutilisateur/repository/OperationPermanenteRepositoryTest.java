package com.cmarchive.bank.serviceutilisateur.repository;


import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@DataMongoTest
public class OperationPermanenteRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OperationPermanenteRepository operationPermanenteRepository;

    @Test
    public void sauvegarderOperationPermanenteAvecUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente = creerOperationPermanente(cyril);
        mongoTemplate.save(cyril);

        Mono<OperationPermanente> resultat = operationPermanenteRepository.save(operationPermanente);

        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNext(operationPermanente)
                .verifyComplete();
    }

    @Test
    public void supprimerOperationPermanente() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente = creerOperationPermanente(cyril);
        mongoTemplate.save(cyril);
        mongoTemplate.save(operationPermanente);

        Mono<Void> resultat = operationPermanenteRepository.deleteById(operationPermanente.getId());

        StepVerifier.create(resultat)
                .expectSubscription()
                .verifyComplete();
        OperationPermanente resultatOperationPermanente = mongoTemplate.findById(operationPermanente.getId(),
                OperationPermanente.class);
        assertThat(resultatOperationPermanente).isNull();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void listerOperationPermanenteParUtilisateur() {
        Utilisateur cyril = creerUtilisateur();
        OperationPermanente operationPermanente1 = creerOperationPermanente(cyril);
        OperationPermanente operationPermanente2 = creerOperationPermanente(cyril);
        operationPermanente2.setJour(2);
        mongoTemplate.save(cyril);
        mongoTemplate.save(operationPermanente1);
        mongoTemplate.save(operationPermanente2);

        Flux<OperationPermanente> resultat = operationPermanenteRepository.findAllByUtilisateur_Id(cyril.getId());

        StepVerifier.create(resultat)
                .expectSubscription()
                .expectNextMatches(operationPermanente -> operationPermanente.getJour() == 12)
                .expectNextMatches(operationPermanente -> operationPermanente.getJour() == 2)
                .verifyComplete();
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