package com.cmarchive.bank.serviceutilisateur.exception;

public class UtilisateurDejaPresentException extends RuntimeException {
    private String message;

    public UtilisateurDejaPresentException(String message) {
        this.message = message;
    }

}
