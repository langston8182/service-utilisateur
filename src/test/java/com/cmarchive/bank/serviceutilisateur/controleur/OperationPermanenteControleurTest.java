package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.model.OperationPermanenteDto;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(OperationPermanenteControleur.class)
@AutoConfigureMockMvc(secure=false)
public class OperationPermanenteControleurTest {

    private static final String ID_OKTA = "idOkta";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OperationPermanenteService operationPermanenteService;

    @MockBean
    private ResourceServerProperties resourceServerProperties;

    @MockBean
    private ObjectPostProcessor objectPostProcessor;

    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    public void ajouterOperationPermanenteAUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationPermanenteDto operationPermanenteDto = creerOperationPermanenteDto(utilisateurDto);
        operationPermanenteDto.setUtilisateurDto(null);
        OperationPermanenteDto reponse = new OperationPermanenteDto()
                .intitule("test")
                .utilisateurDto(utilisateurDto);
        given(operationPermanenteService.ajouterOperationPermanenteAUtilisateur(
                anyString(), any(OperationPermanenteDto.class)))
                .willReturn(reponse);

        mockMvc.perform(post("/operations-permanentes/")
                .principal(getPincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationPermanenteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")));
    }

    @Test
    public void modifierOperationPermanenteUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationPermanenteDto operationPermanenteDto = creerOperationPermanenteDto(utilisateurDto);
        OperationPermanenteDto reponse = new OperationPermanenteDto()
                .intitule("test")
                .utilisateurDto(utilisateurDto);
        given(operationPermanenteService.modifierOperationPermanenteUtilisateur(anyString(), any(OperationPermanenteDto.class)))
                .willReturn(reponse);

        mockMvc.perform(put("/operations-permanentes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationPermanenteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")));
    }

    @Test
    public void supprimerOperationPermanenteUtilisateur() throws Exception {
        willDoNothing().given(operationPermanenteService).supprimerOperationPermanente("1");

        mockMvc.perform(delete("/operations-permanentes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    private OperationPermanenteDto creerOperationPermanenteDto(UtilisateurDto cyril) {
        return new OperationPermanenteDto()
                .jour(12)
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

    private Principal getPincipal() {
        return () -> ID_OKTA;
    }
}