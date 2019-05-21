package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.OperationPermanente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface CRUD pour manipuler des operations permanentes.
 */
@Repository
public interface OperationPermanenteRepository extends JpaRepository<OperationPermanente, String> {

    List<OperationPermanente> findAllByUtilisateur_Id(String utilisateurId);
    List<OperationPermanente> findAllByUtilisateur_Email(String email);

}
