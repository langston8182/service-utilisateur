package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.ressource.model.UtilisateurDtos;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateurs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UtilisateursMapperTest {

    @Autowired
    private UtilisateursMapper utilisateursMapper;

    @Configuration
    @ComponentScan(basePackageClasses = UtilisateursMapper.class, useDefaultFilters = false, includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UtilisateursMapper.class,
                    UtilisateurMapper.class})})
    public static class TestConfiguration {
        //
    }

    @Test
    public void mapVersUtilisateurs() {
        UtilisateurDto utilisateurDto = new UtilisateurDto()
                .email("cyril.marchive@gmail.com")
                .nom("Marchive")
                .prenom("Cyril");
        UtilisateurDtos utilisateursDto = new UtilisateurDtos()
                .utilisateurDtos(singletonList(utilisateurDto));

        Utilisateurs resultat = utilisateursMapper.mapVersUtilisateurs(utilisateursDto);

        assertThat(resultat.getUtilisateurs()).hasSize(1);
        assertThat(resultat.getUtilisateurs().get(0)).isEqualToComparingFieldByField(utilisateurDto);
    }

    @Test
    public void mapVersUtilisateursDto() {
        Utilisateur utilisateur = new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
        Utilisateurs utilisateurs = new Utilisateurs()
                .setUtilisateurs(singletonList(utilisateur));

        UtilisateurDtos resultat = utilisateursMapper.mapVersUtilisateurDtos(utilisateurs);

        assertThat(resultat.getUtilisateurDtos()).hasSize(1);
        assertThat(resultat.getUtilisateurDtos().get(0)).isEqualToComparingOnlyGivenFields(utilisateur,
                "nom", "prenom", "email");
    }
}