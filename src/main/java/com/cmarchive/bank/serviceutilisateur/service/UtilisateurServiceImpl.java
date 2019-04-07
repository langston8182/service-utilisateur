package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements  UtilisateurService {

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
    public UtilisateursDto listerUtilisateurs() {
        Utilisateurs utilisateurs = new Utilisateurs()
                .setUtilisateurs(utilisateurRepository.findAll());

        return utilisateursMapper.mapVersUtilisateursDto(utilisateurs);
    }

    @Override
    public UtilisateurDto recupererUtilisateur(String id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNonTrouveException("L'utilisateur n'a pas ete trouve"));
        return utilisateurMapper.mapVersUtilisateurDto(utilisateur);
    }

    @Override
    public UtilisateurDto sauvegarderUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);
        Utilisateur reponse = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.mapVersUtilisateurDto(reponse);
    }

    @Override
    public void supprimerUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);
        utilisateurRepository.delete(utilisateur);
    }
}
