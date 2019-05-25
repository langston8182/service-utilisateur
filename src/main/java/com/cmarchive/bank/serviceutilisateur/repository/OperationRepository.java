package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface CRUD pour manipuler des operations.
 */
@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {

    List<Operation> findAllByUtilisateur_IdOrderByDateOperationDesc(String id);
    List<Operation> findAllByUtilisateur_EmailOrderByDateOperationDesc(String email);
    Operation findByUtilisateur_IdAndId(String idUtilisateur, String idOperation);

}
