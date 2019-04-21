package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.OperationsDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
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
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(OperationControleur.class)
@AutoConfigureMockMvc(secure=false)
public class OperationControleurTest {

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
    public void listerOperationUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto1 = creerOperationDto(utilisateurDto);
        OperationDto operationDto2 = creerOperationDto(utilisateurDto);
        OperationsDto operationsDto = new OperationsDto()
                .setOperationDtos(Stream.of(operationDto1, operationDto2).collect(Collectors.toList()));
        given(operationService.listerOperationsParUtilisateur("1")).willReturn(operationsDto);

        mockMvc.perform(get("/operations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationDtos", hasSize(2)));
    }

    @Test
    public void ajouterOperationAUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        OperationDto reponse = new OperationDto()
                .setIntitule("test")
                .setUtilisateurDto(utilisateurDto);
        given(operationService.ajouterOperationAUtilisateur(anyString(), any(OperationDto.class)))
                .willReturn(reponse);

        mockMvc.perform(post("/operations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")));
    }

    @Test
    public void modifierOperationUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        OperationDto reponse = new OperationDto()
                .setIntitule("test")
                .setUtilisateurDto(utilisateurDto);
        given(operationService.modifierOperationUtilisateur(any(OperationDto.class)))
                .willReturn(reponse);

        mockMvc.perform(put("/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule", equalTo("test")))
                .andExpect(jsonPath("$.utilisateurDto.prenom", equalTo("Cyril")));
    }

    @Test
    public void supprimerOperationUtilisateur() throws Exception {
        UtilisateurDto utilisateurDto = creerUtilisateurDto();
        OperationDto operationDto = creerOperationDto(utilisateurDto);
        willDoNothing().given(operationService).supprimerOperation(operationDto);

        mockMvc.perform(delete("/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDto)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    private OperationDto creerOperationDto(UtilisateurDto cyril) {
        return new OperationDto()
                .setDateOperation(LocalDate.now())
                .setIntitule("operation")
                .setPrix(BigDecimal.TEN)
                .setUtilisateurDto(cyril);
    }

    private UtilisateurDto creerUtilisateurDto() {
        return new UtilisateurDto()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }
}