package com.cmarchive.bank.serviceutilisateur.modele;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Operation bancaire. Peut etre soit un credit(salaire, ...) ou un debit(factures, ...).
 */
@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String intitule;
    private LocalDate dateOperation;
    private BigDecimal prix;

    @ManyToOne
    private Utilisateur utilisateur;

    public String getId() {
        return id;
    }

    public Operation setId(String id) {
        this.id = id;
        return this;
    }

    public String getIntitule() {
        return intitule;
    }

    public Operation setIntitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public LocalDate getDateOperation() {
        return dateOperation;
    }

    public Operation setDateOperation(LocalDate dateOperation) {
        this.dateOperation = dateOperation;
        return this;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public Operation setPrix(BigDecimal prix) {
        this.prix = prix;
        return this;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Operation setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }
}
