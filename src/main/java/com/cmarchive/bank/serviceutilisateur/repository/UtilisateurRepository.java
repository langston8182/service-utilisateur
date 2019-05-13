package com.cmarchive.bank.serviceutilisateur.repository;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface CRUD pour manipuler des utilisateurs.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Optional<Utilisateur> findByEmail(String email);

}
