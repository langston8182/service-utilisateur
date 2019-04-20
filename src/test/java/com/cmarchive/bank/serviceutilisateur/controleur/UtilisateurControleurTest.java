package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateurDto;
import com.cmarchive.bank.serviceutilisateur.modele.dto.UtilisateursDto;
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@AutoConfigureMockMvc(secure=false)
public class UtilisateurControleurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private ResourceServerProperties resourceServerProperties;

    @MockBean
    private ObjectPostProcessor objectPostProcessor;

    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    public void listerUtilisateurs() throws Exception {
        UtilisateurDto cyril = new UtilisateurDto()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
        UtilisateurDto melanie = new UtilisateurDto()
                .setEmail("melanie.boussat@gmail.com")
                .setNom("Boussat")
                .setPrenom("Melanie");
        UtilisateursDto utilisateursDto = new UtilisateursDto()
                .setUtilisateursDtos(Stream.of(cyril, melanie).collect(Collectors.toList()));
        given(utilisateurService.listerUtilisateurs()).willReturn(utilisateursDto);

        mockMvc.perform(get("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.utilisateursDtos", hasSize(2)));
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
    public void supprimerUtilisateur() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        willDoNothing().given(utilisateurService).supprimerUtilisateur(cyril);

        mockMvc.perform(delete("/utilisateurs")
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
                .setId("1");
        given(utilisateurService.creerUtilisateur(any(UtilisateurDto.class))).willReturn(reponse);

        mockMvc.perform(post("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("1")));
    }

    @Test
    public void modifierUtilisateur() throws Exception {
        UtilisateurDto cyril = creerUtilisateurDto();
        UtilisateurDto reponse = creerUtilisateurDto()
                .setNom("Boussat");
        given(utilisateurService.modifierUtilisateur(any(UtilisateurDto.class))).willReturn(reponse);

        mockMvc.perform(put("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Boussat")));
    }

    private UtilisateurDto creerUtilisateurDto() {
        return new UtilisateurDto()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }
}