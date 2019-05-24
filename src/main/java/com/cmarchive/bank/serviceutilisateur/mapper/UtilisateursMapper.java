package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.UtilisateurDtos;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UtilisateurMapper.class)
public interface UtilisateursMapper {

    @Mapping(target = "utilisateurs", source = "utilisateurDtos")
    Utilisateurs mapVersUtilisateurs(UtilisateurDtos utilisateurDtos);

    @Mapping(target = "utilisateurDtos", source = "utilisateurs")
    UtilisateurDtos mapVersUtilisateurDtos(Utilisateurs utilisateurs);

}
