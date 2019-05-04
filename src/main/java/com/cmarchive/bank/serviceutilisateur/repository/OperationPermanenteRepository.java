package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Interface CRUD pour manipuler des operations permanentes.
 */
@Repository
public interface OperationPermanenteRepository extends ReactiveMongoRepository<OperationPermanente, String> {

    Flux<OperationPermanente> findAllByUtilisateur_Id(String utilisateurId);

}
