package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    Utilisateur mapVersUtilisateur(UtilisateurDto utilisateurDto);
    UtilisateurDto mapVersUtilisateurDto(Utilisateur utilisateur);

}
