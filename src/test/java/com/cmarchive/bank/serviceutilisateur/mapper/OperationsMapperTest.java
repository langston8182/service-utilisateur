package com.cmarchive.bank.serviceutilisateur.mapper;

import com.cmarchive.bank.ressource.model.OperationDto;
import com.cmarchive.bank.ressource.model.OperationDtos;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.Operation;
import com.cmarchive.bank.serviceutilisateur.modele.Operations;
import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OperationsMapperTest {

    @Autowired
    private OperationsMapper operationsMapper;

    @Configuration
    @ComponentScan(basePackageClasses = OperationsMapper.class, useDefaultFilters = false, includeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OperationsMapper.class,
                    OperationMapper.class, UtilisateurMapper.class})})
    public static class TestConfiguration {
        //
    }

    @Test
    public void mapVersOperations() {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        OperationDtos operationsDto = new OperationDtos()
                .operationDtos(singletonList(operationDto));

        Operations resultat = operationsMapper.mapVersOperations(operationsDto);

        assertThat(resultat.getOperations()).hasSize(1);
        assertThat(resultat.getOperations().get(0).getUtilisateur()).isEqualToComparingFieldByField(utilisateurDto);
        assertThat(resultat.getOperations().get(0))
                .isEqualToIgnoringGivenFields(operationDto, "utilisateur");
    }

    @Test
    public void mapVersOperationsDto() {
        Utilisateur utilisateur = creerUtilisateur();
        Operation operation = creerOperation(utilisateur);
        Operations operations = new Operations()
                .setOperations(singletonList(operation));

        OperationDtos resultat = operationsMapper.mapVersOperationDtos(operations);

        assertThat(resultat.getOperationDtos()).hasSize(1);
        assertThat(resultat.getOperationDtos().get(0).getUtilisateurDto()).isEqualToComparingFieldByField(utilisateur);
        assertThat(resultat.getOperationDtos().get(0))
                .isEqualToIgnoringGivenFields(operation, "utilisateurDto");
    }

    private Operation creerOperation(Utilisateur cyril) {
        return new Operation()
                .setDateOperation(LocalDate.now())
                .setIntitule("operation")
                .setPrix(BigDecimal.TEN)
                .setUtilisateur(cyril);
    }

    private Utilisateur creerUtilisateur() {
        return new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }

    private OperationDto creerOperationDto(UtilisateurDto cyril) {
        return new OperationDto()
                .dateOperation(LocalDate.now())
                .intitule("operation")
                .prix(BigDecimal.TEN)
                .utilisateurDto(cyril);
    }

    private UtilisateurDto creerUtilisateurDto() {
        return new UtilisateurDto()
                .email("cyril.marchive@gmail.com")
                .nom("Marchive")
                .prenom("Cyril");
    }
}