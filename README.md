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

Le serveur de resources est en charge de verifier que les token JWT sont corrects.

![okta application selection](images/Sélection_003.png)

![okta application selection](images/Sélection_004.png)

Le serveur de resources n'est pas capable de fournir un token JWT. Pour cela, un serveur d'authentification est necessaire.

Creer une application pour le serveur d'authentification en choisissant **Single Page Application** afin que le client s'authentifie pour une application React (Grant type implicit)
![okta application selection](images/Sélection_005.png)

![okta application selection](images/Sélection_006.png)

![okta application selection](images/Sélection_007.png)

#### Serveur de resource
Ajouter les configurations okta dans le fichier **application.yml**

Recuperer le *clientId* et *clientSecret* du serveur de resources cree precedement.
```
okta:
  oauth2:
    issuer: https://dev-847930.okta.com/oauth2/default
    clientId: 0oalbqv5tDtNvYWwY356
    clientSecret: kKdzYKFoS9GBhiY7G3ryS2XCzHCuUs8TJX6HACOr
```

#### Postman
Pour recuperer le token JWT, il faut creer une applicatin Okta de type Web (Grant type AuthorizationCode)

![okta application selection](images/Sélection_008.png)

Pour recuperer un nouveau token Postman, aller dans l'onclet Authorization choisir le type Oatuh2 puis cliquer sur **Get new access token**

![okta postman](images/Sélection_009.png)

![okta postman](images/Sélection_010.png)

#### Maven
Ajouter dans le *pom.xml*
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.1.4.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-oauth2-resource-server</artifactId>
	<version>5.1.5.RELEASE</version>
</dependency>
<dependency>
	<groupId>com.okta.spring</groupId>
	<artifactId>okta-spring-boot-starter</artifactId>
	<version>0.6.1</version>
</dependency>
```

# Contributeur
Cyril Marchive (cyril.marchive@gmail.com)
