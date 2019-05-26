package com.cmarchive.bank.serviceutilisateur.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.Principal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Configuration
@ConfigurationProperties(prefix = "swagger.doc")
@EnableSwagger2
@Data
public class SwaggerConfig {

    private String tokenEndPoint;
    private String authorizeEndPoint;
    private String clientId;
    private String clientSecret;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(singletonList(securityScheme()))
                .securityContexts(singletonList(securityContext()))
                .apiInfo(apiInfo())
                .ignoredParameterTypes(Principal.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Api service utilisateur",
                "Api de gestion des utilisateurs et operations bancaires",
                "1.0.0",
                null,
                new Contact("Cyril Marchive", "https://github.com/langston8182", "cyril.marchive@gmail.com"),
                "",
                "",
                emptyList());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(asList(new SecurityReference("security_oauth", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(tokenEndPoint, "oauthtoken"))
                .tokenRequestEndpoint(new TokenRequestEndpoint(authorizeEndPoint, clientId, clientSecret))
                .build();

        SecurityScheme securityScheme = new OAuthBuilder().name("spring_oauth")
                .grantTypes(asList(grantType))
                .scopes(asList(scopes()))
                .build();

        return securityScheme;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
          new AuthorizationScope("admin", "pour les operation d'administration"),
          new AuthorizationScope("openid", "pour les operation liee a Okta")
        };

        return scopes;
    }

}
