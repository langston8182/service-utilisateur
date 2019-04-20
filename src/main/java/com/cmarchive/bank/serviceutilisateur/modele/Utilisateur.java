package com.cmarchive.bank.serviceutilisateur.modele;

import javax.persistence.*;

/**
 * Utilisateur de l'application bank;
 */
@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;
    private String motDePasse;

    public Utilisateur() {
        this.id = id;
    }

    public Utilisateur setId(String id) {
        this.id = id;
        return this;
    }

    public Utilisateur setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public Utilisateur setPrenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public Utilisateur setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public Utilisateur setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
        return this;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}
