package com.cmarchive.bank.serviceutilisateur.modele;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Operation bancaire. Peut etre soit un credit(salaire, ...) ou un debit(factures, ...).
 */
@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String intitule;
    private LocalDate dateOperation;
    private BigDecimal prix;

    @ManyToOne
    private Utilisateur utilisateur;
}
