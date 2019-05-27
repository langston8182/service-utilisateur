package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.ressource.model.UtilisateurDtos;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private UtilisateursMapper utilisateursMapper;
    private UtilisateurMapper utilisateurMapper;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  UtilisateursMapper utilisateursMapper,
                                  UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateursMapper = utilisateursMapper;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Override
    public UtilisateurDtos listerUtilisateurs() {
        Utilisateurs utilisateurs = new Utilisateurs()
                .setUtilisateurs(utilisateurRepository.findAll());

        return utilisateursMapper.mapVersUtilisateurDtos(utilisateurs);
    }

    @Override
    public UtilisateurDto recupererUtilisateurParEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UtilisateurNonTrouveException("L'utilisateur n'a pas ete trouve"));
        return utilisateurMapper.mapVersUtilisateurDto(utilisateur);
    }

    @Override
    public UtilisateurDto recupererUtilisateur(String id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNonTrouveException("L'utilisateur n'a pas ete trouve"));
        return utilisateurMapper.mapVersUtilisateurDto(utilisateur);
    }

    @Override
    public UtilisateurDto creerUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);

        Utilisateur reponse;
        try {
            reponse = utilisateurRepository.save(utilisateur);
        } catch (DataIntegrityViolationException dive) {
            throw new UtilisateurDejaPresentException("L'utilisateur est deja présent");
        }
        return utilisateurMapper.mapVersUtilisateurDto(reponse);
    }

    @Override
    public UtilisateurDto modifierUtilisateur(String idUtilisateur, UtilisateurDto utilisateurDto) {
        recupererUtilisateur(idUtilisateur);
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);

        Utilisateur reponse = utilisateurRepository.save(utilisateur);

        return utilisateurMapper.mapVersUtilisateurDto(reponse);
    }

    @Override
    public void supprimerUtilisateur(String id) {
        utilisateurRepository.deleteById(id);
    }
}
