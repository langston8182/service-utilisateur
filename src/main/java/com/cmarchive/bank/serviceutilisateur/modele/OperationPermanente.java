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

/**
 * Opération permanentes liées a un utilisateur. Ces opérations se repetent chaque mois en general
 * au meme prix et a la meme date. Il s'agit principalement de factures ou les salaires.
 */
@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OperationPermanente {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String intitule;
    private Integer jour;
    private BigDecimal prix;

    @ManyToOne
    private Utilisateur utilisateur;

}
