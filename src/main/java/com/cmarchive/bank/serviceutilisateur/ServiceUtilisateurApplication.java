package com.cmarchive.bank.serviceutilisateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableConfigurationProperties
@EnableResourceServer
public class ServiceUtilisateurApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceUtilisateurApplication.class, args);
	}

}
