package com.cmarchive.bank.serviceutilisateur.modele;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String intitule;
    private LocalDate dateOperation;
    private BigDecimal prix;

    @ManyToOne
    private Utilisateur utilisateur;
}
