# Version

- **1.0.0-SNAPSHOT** : initialisation du projet

# Objectif

Serveur de resources permettant de gérer les utilisateurs de l'application bancaires.
\
Utilise les verbes CRUD pour la gestion des utilisateurs.

# Technique

Nécéssite une connection oauth2 et d'un jeton JWT pour fonctionner :
https://github.com/langston8182/bank-oauth2-authorization-server
Il faut avant tout démarrer le serveur d'autorisation.

Port d'écoute : 8100

**Postman** :
Se loguer pour récupérer le token JWT

![postman](images/postman.png)

# Docker

Utiliser docker-compose pour lancer le serveur.
`docker-compose up`

# Maven

Pour compiler le projet et créer l'image docker exécuter :
\
`mvn package`

Pour push l'image sur dockerHub, exécuter:
\
`mvn dockerfile:push`

# Configuration

Dans le fichier application.yml modifier les adresses Ip suivant si le serveur tourne en local ou dans un container.

Ip serveur autorisation dans un container : 172.21.0.1

# Contributeur

Cyril Marchive (cyril.marchive@gmail.com)
