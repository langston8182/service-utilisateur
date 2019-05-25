package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.ressource.model.*;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurDejaPresentException;
import com.cmarchive.bank.serviceutilisateur.exception.UtilisateurNonTrouveException;
import com.cmarchive.bank.serviceutilisateur.service.OperationPermanenteService;
import com.cmarchive.bank.serviceutilisateur.service.OperationService;
import com.cmarchive.bank.serviceutilisateur.service.UtilisateurService;
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

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(UtilisateurControleur.class)
@AutoConfigureMockMvc(secure = false)
public class UtilisateurControleurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private OperationService operationService;

    @MockBean
    private OperationPermanenteService operationPermanenteService;

    @MockBean
    private ResourceServerProperties resourceServerProperties;

    @MockBean
    private ObjectPostProcessor objectPostProcessor;

    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    public void listerUtilisateurs() throws Exception {
        UtilisateurDto cyril = new UtilisateurDto()
                .email("cyril.marchive@gmail.com")
                .nom("Marchive")
                .prenom("Cyril");
        UtilisateurDto melanie = new UtilisateurDto()
                .email("melanie.boussat@gmail.com")
                .nom("Boussat")
                .prenom("Melanie");
        UtilisateurDtos utilisateursDto = new UtilisateurDtos()
                .utilisateurDtos(Stream.of(cyril, melanie).collect(Collectors.toList()));
        given(utilisateurService.listerUtilisateurs()).willReturn(utilisateursDto);

        mockMvc.perform(get("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.utilisateurDtos", hasSize(2)));
    }

    @Test
    public void recupererUtilisateur() throws Exception {
        UtilisateurDto utilisateur = creerUtilisateurDto();
        given(utilisateurService.recupererUtilisateur("1")).willReturn(utilisateur);

        mockMvc.perform(get("/utilisateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Marchive")))
                .andExpect(jsonPath("$.prenom", equalTo("Cyril")))
                .andExpect(jsonPath("$.email", equalTo("cyril.marchive@gmail.com")));
    }

    @Test
    public void recupererUtilisateur_UtilisateurInexistant() throws Exception {
        given(utilisateurService.recupererUtilisateur("1")).willThrow(UtilisateurNonTrouveException.class);

        mockMvc.perform(get("/utilisateurs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void recupererUtilisateurParEmail() throws Exception {
        UtilisateurDto utilisateur = creerUtilisateurDto();
        given(utilisateurService.recupererUtilisateurParEmail("cyril.marchive@gmail.com")).willReturn(utilisateur);

        mockMvc.perform(get("/utilisateurs?email=cyril.marchive@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Marchive")))
                .andExpect(jsonPath("$.prenom", equalTo("Cyril")))
                .andExpect(jsonPath("$.email", equalTo("cyril.marchive@gmail.com")));
    }

    @Test
    public void recupererUtilisateurParEmail_UtilisateurInexistant() throws Exception {
        given(utilisateurService.recupererUtilisateurParEmail("cyril.marchive@gmail.com")).willThrow(UtilisateurNonTrouveException.class);

        mockMvc.perform(get("/utilisateurs?email=cyril.marchive@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void supprimerUtilisateur() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        willDoNothing().given(utilisateurService).supprimerUtilisateur(cyril);

        mockMvc.perform(delete("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void sauvegarderUtilisateur() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        UtilisateurDto reponse = new UtilisateurDto()
                .identifiant("1");
        given(utilisateurService.creerUtilisateur(any(UtilisateurDto.class))).willReturn(reponse);

        mockMvc.perform(post("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identifiant", equalTo("1")));
    }

    @Test
    public void sauvegarderUtilisateur_UtilisateurDejaExistant() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        given(utilisateurService.creerUtilisateur(any(UtilisateurDto.class)))
                .willThrow(UtilisateurDejaPresentException.class);

        mockMvc.perform(post("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isConflict());
    }

    @Test
    public void modifierUtilisateur() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        UtilisateurDto reponse = creerUtilisateurDto()
                .nom("Boussat");
        given(utilisateurService.modifierUtilisateur(any(UtilisateurDto.class))).willReturn(reponse);

        mockMvc.perform(put("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Boussat")));
    }

    @Test
    public void modifierUtilisateur_UtilisateurInexistant() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        given(utilisateurService.modifierUtilisateur(any(UtilisateurDto.class))).willThrow(UtilisateurNonTrouveException.class);

        mockMvc.perform(put("/utilisateurs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listerOperationPermanenteUtilisateur() throws Exception {
        OperationPermanenteDto operationPermanenteDto = creerOperationPermanenteDto();
        OperationPermanenteDtos operationPermanenteDtos = new OperationPermanenteDtos()
                .operationPermanenteDtos(singletonList(operationPermanenteDto));
        given(operationPermanenteService.listerOperationPermanentesParUtilisateur("1")).willReturn(operationPermanenteDtos);

        mockMvc.perform(get("/utilisateurs/1/operations-permanentes/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationPermanenteDtos", hasSize(1)))
                .andExpect(jsonPath("$.operationPermanenteDtos[0].intitule", equalTo("Salaire")));
    }

    @Test
    public void recupererOperationPermanenteUtilisateur() throws Exception {
        OperationPermanenteDto operationPermanenteDto = creerOperationPermanenteDto();
        given(operationPermanenteService.recupererOperationPermanenteParUtilisateur("1", "2"))
                .willReturn(operationPermanenteDto);

        mockMvc.perform(get("/utilisateurs/1/operations-permanentes/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule", equalTo("Salaire")))
                .andExpect(jsonPath("$.utilisateurDto.nom", equalTo("Marchive")));
    }

    @Test
    public void recupererOperationPermanenteUtilisateur_UtilisateurNonExistant() throws Exception {
        given(operationPermanenteService.recupererOperationPermanenteParUtilisateur("1", "2"))
                .willThrow(UtilisateurNonTrouveException.class);

        mockMvc.perform(get("/utilisateurs/1/operations-permanentes/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listerOperationUtilisateur() throws Exception {
        OperationDto operationDto = creerOperationDto();
        OperationDtos operationDtos = new OperationDtos()
                .operationDtos(singletonList(operationDto));
        given(operationService.listerOperationsParUtilisateur("1")).willReturn(operationDtos);

        mockMvc.perform(get("/utilisateurs/1/operations/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationDtos", hasSize(1)))
                .andExpect(jsonPath("$.operationDtos[0].intitule", equalTo("Hydro")));
    }

    @Test
    public void recupererOperationUtilisateur() throws Exception {
        OperationDto operationDto = creerOperationDto();
        given(operationService.recupererOperationParUtilisateur("1", "2")).willReturn(operationDto);

        mockMvc.perform(get("/utilisateurs/1/operations/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule", equalTo("Hydro")))
                .andExpect(jsonPath("$.utilisateurDto.nom", equalTo("Marchive")));
    }

    @Test
    public void recupererOperationUtilisateur_UtilisateurNonExistant() throws Exception {
        given(operationService.recupererOperationParUtilisateur("1", "2")).willThrow(UtilisateurNonTrouveException.class);

        mockMvc.perform(get("/utilisateurs/1/operations/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private OperationDto creerOperationDto() {
        return new OperationDto()
                .intitule("Hydro")
                .dateOperation(LocalDate.now())
                .prix(BigDecimal.TEN)
                .utilisateurDto(creerUtilisateurDto());
    }

    private OperationPermanenteDto creerOperationPermanenteDto() {
        return new OperationPermanenteDto()
                .intitule("Salaire")
                .jour(12)
                .prix(BigDecimal.TEN)
                .utilisateurDto(creerUtilisateurDto());
    }

    private UtilisateurDto creerUtilisateurDto() {
        return new UtilisateurDto()
                .email("cyril.marchive@gmail.com")
                .nom("Marchive")
                .prenom("Cyril");
    }
}