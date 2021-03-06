package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.model.OperationDto;
import com.cmarchive.bank.ressource.model.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
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
import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(OperationControleur.class)
@AutoConfigureMockMvc(secure=false)
public class OperationControleurTest {

    private static final String ID_OKTA = "idOkta";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OperationService operationService;

    @MockBean
    private ResourceServerProperties resourceServerProperties;

    @MockBean
    private ObjectPostProcessor objectPostProcessor;

    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    public void ajouterOperationAUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        operationDto.setUtilisateurDto(null);
        OperationDto reponse = new OperationDto()
                .intitule("test")
                .utilisateurDto(utilisateurDto);
        given(operationService.ajouterOperationAUtilisateur(anyString(), any(OperationDto.class)))
                .willReturn(reponse);

        mockMvc.perform(post("/operations/")
                .principal(getPincipal())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")))
        .andDo(print());
    }

    @Test
    public void modifierOperationUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        OperationDto reponse = new OperationDto()
                .intitule("test")
                .utilisateurDto(utilisateurDto);
        given(operationService.modifierOperationUtilisateur(anyString(), any(OperationDto.class)))
                .willReturn(reponse);

        mockMvc.perform(put("/operations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")));
    }

    @Test
    public void supprimerOperationUtilisateur() throws Exception {
        willDoNothing().given(operationService).supprimerOperation("1");

        mockMvc.perform(delete("/operations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
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

    private Principal getPincipal() {
        return () -> ID_OKTA;
    }
}