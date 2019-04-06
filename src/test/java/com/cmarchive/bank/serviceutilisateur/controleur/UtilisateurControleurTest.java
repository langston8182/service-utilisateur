package com.cmarchive.bank.serviceutilisateur.controleur;

import com.cmarchive.bank.serviceutilisateur.modele.Utilisateur;
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

import java.util.List;
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
        Utilisateur cyril = new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
        Utilisateur melanie = new Utilisateur()
                .setEmail("melanie.boussat@gmail.com")
                .setNom("Boussat")
                .setPrenom("Melanie");
        List<Utilisateur> utilisateurs = Stream.of(cyril, melanie).collect(Collectors.toList());
        given(utilisateurService.listerUtilisateurs()).willReturn(utilisateurs);

        mockMvc.perform(get("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void recupererUtilisateur() throws Exception {
        Utilisateur utilisateur = creerUtilisateur();
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
        Utilisateur cyril = creerUtilisateur();
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
        Utilisateur cyril = creerUtilisateur();
        Utilisateur reponse = new Utilisateur()
                .setId("1");
        given(utilisateurService.sauvegarderUtilisateur(any(Utilisateur.class))).willReturn(reponse);

        mockMvc.perform(post("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("1")));
    }

    @Test
    public void modifierUtilisateur() throws Exception {
        Utilisateur cyril = creerUtilisateur();
        Utilisateur reponse = creerUtilisateur()
                .setNom("Boussat");
        given(utilisateurService.sauvegarderUtilisateur(any(Utilisateur.class))).willReturn(reponse);

        mockMvc.perform(put("/utilisateurs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cyril)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Boussat")));
    }

    private Utilisateur creerUtilisateur() {
        return new Utilisateur()
                .setEmail("cyril.marchive@gmail.com")
                .setNom("Marchive")
                .setPrenom("Cyril");
    }
}