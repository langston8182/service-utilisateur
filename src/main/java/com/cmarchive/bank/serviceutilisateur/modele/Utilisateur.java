package com.cmarchive.bank.serviceutilisateur.modele;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Utilisateur de l'application bank;
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;
    private String motDePasse;
}
