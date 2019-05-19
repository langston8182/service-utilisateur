# Version

- **1.1.0-SNAPSHOT** :
- **1.0.0** : initialisation du projet

# Objectif
Serveur de resources permettant de gérer les utilisateurs de l'application bancaires.
Utilise les verbes **CRUD** pour la gestion des utilisateurs.

# Technique
Nécéssite une connection oauth2 et d'un jeton JWT pour fonctionner :
https://github.com/langston8182/bank-oauth2-authorization-server
Il faut avant tout démarrer le serveur d'autorisation.

>Port d'écoute : 8100

# Postman :
Se loguer pour récupérer le token JWT

![postman](images/postman.png)

# Docker
Utiliser docker-compose pour lancer le serveur.
```sh
$ docker-compose up
```

# Kubernetes
L'ensemble des configurations k8s se trouvent dans le projet Gihub
https://github.com/langston8182/kubernetes-bank

Pour lancer le service
```sh
$ kubectl create -f utilisateur-service.yaml
```

Pour lancer le deploiement
```sh
$ kubectl create -f utilisateur-deployment.yaml
```

# Maven
- Pour compiler le projet :
```sh
$ mvn clean install
```

- Pour construire l'image Docker :
```sh
$ mvn dockerfile:build
```

- Pour push l'image sur dockerHub, exécuter:
```sh
$ mvn dockerfile:push
```

# Concourse
Concourse est un outil pour faire du développement continue. A l'instar de Jenkins, 
il s'agit d'un pipeline as code. Fly est un interpreteur de commandes pour manipuler les pipelines concourse.

### Installation :
https://github.com/concourse/concourse-docker

#### Commandes fly:

- Créer un token de connexion
```sh
$ fly login --concourse-url http://127.0.0.1:8080 -u test -p test
```
- Lister les targets
```sh
$ fly targets
```

Pour chaque commande qui va suivre il faut suivre fly du target (fly -t tutorial).

- Créer un alias fly
```sh
$ alias fly="fly -t tutorial"
```
- Créer un pipeline a partir d'un fichier de configuration\
```sh
$ fly sp -p service-utilisateur -c ci/pipeline.yml -l credentials.yml
```

#### Utilisation :
Pour le projet, un triger est lance dès que un commit est fait sur la branche master.
Le build du projet a lieu (mvn clean install), puis a l'issue de ce build, une image docker du jar est crée.
Cette image est ensuite déployé sur le registry docker.

# Configuration
Dans le fichier application.yml modifier les adresses Ip suivant si le serveur tourne en local ou dans un container.
Ip du serveur autorisation dans un container : **172.21.0.1**
Ip du servuer autorisation dans k8s : **authorization-service**

# Okta
Okta est une solution **IDass** (Identity as a Service) qui permet de gerer les authentifications et les autorisations en utilisant OpenId Connect.

#### Serveur autorisation
Il faut commencer par creer un serveur d'autorisation
![okta autorisation](images/Sélection_002.png)

#### Applications
Creer une application pour le serveur de resource en choisissant **Service Machine to machine**

![okta application selection](images/Sélection_003.png)
![okta application selection](images/Sélection_004.png)

Creer une application pour le serveur d'authentification en choisissant **Single Page Application** afin que le client s'authentifie (Application React)
![okta application selection](images/Sélection_005.png)
![okta application selection](images/Sélection_006.png)
![okta application selection](images/Sélection_007.png)

#### Serveur de resource
Ajouter le Jwk (Json Web Key) dans les parametres du serveur pour permettre l'autorisation au serveur Okta
```
spring:
  application:
    name: utilisateur
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://dev-847930.okta.com/oauth2/default
          jwk-set-uri: https://dev-847930.okta.com/oauth2/default/v1/keys
```

# Contributeur
Cyril Marchive (cyril.marchive@gmail.com)
