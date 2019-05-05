package com.cmarchive.bank.serviceutilisateur.service;

import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateurMapper;
import com.cmarchive.bank.serviceutilisateur.mapper.UtilisateursMapper;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private UtilisateurMapper utilisateurMapper;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Override
    public Flux<UtilisateurDto> listerUtilisateurs() {
        return utilisateurRepository.findAll()
                .map(utilisateur -> utilisateurMapper.mapVersUtilisateurDto(utilisateur));
    }

    @Override
    public Mono<UtilisateurDto> recupererUtilisateur(String id) {
        /*Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNonTrouveException("L'utilisateur n'a pas ete trouve"));*/
        return utilisateurRepository.findById(id)
                .map(utilisateur -> utilisateurMapper.mapVersUtilisateurDto(utilisateur));
    }

    @Override
    public Mono<UtilisateurDto> creerUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);

        /*Utilisateur reponse;
        try {
            reponse = utilisateurRepository.save(utilisateur);
        } catch (DataIntegrityViolationException dive) {
            throw new UtilisateurDejaPresentException("L'utilisateur est deja présent");
        }*/
        return utilisateurRepository.save(utilisateur)
                .map(u -> utilisateurMapper.mapVersUtilisateurDto(u));
    }

    @Override
    public Mono<UtilisateurDto> modifierUtilisateur(UtilisateurDto utilisateurDto) {
        return recupererUtilisateur(utilisateurDto.getId())
                .map(uDto -> utilisateurMapper
                        .mapVersUtilisateur(utilisateurDto).setMotDePasse(uDto.getMotDePasse()))
                .flatMap(utilisateur -> utilisateurRepository.save(utilisateur))
                .map(utilisateur -> utilisateurMapper.mapVersUtilisateurDto(utilisateur));
    }

    @Override
    public Mono<Void> supprimerUtilisateur(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = utilisateurMapper.mapVersUtilisateur(utilisateurDto);
        return utilisateurRepository.delete(utilisateur);
    }
}
