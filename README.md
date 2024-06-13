# Tauri

## Présentation

Ce projet a pour but de développer une application afin de permettre une gestion de projet, des équipes, des notes... pour la matière Projet Génie Logiciel !



## Lancement de l'application en local

### BDD

1. Avoir installé MariaDB ou MYSQL sur sa machine, sur le port 3306 de préférence. Sinon, modifier le l'adresse de la BDD dans le fichier `backend/src/main/resources/application/properties`, variable `spring.datasource.url`

### Backend, avec Intellij

1. Installer le JDK 17 :

https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

2. Configurer le projet :

- `File > Project Structure > Project`
- Choisir le JDK 17
- Choisir le niveau de langage `SDK default`

3. Cliquer sur `Import Gradle Project` dans la fenêtre de notification

4. Copier/coller le fichier `backend/src/main/resources/.env.example` et renomer en `.env`. Puis ajouter l'utilisateur et mot de passe de la BDD.

5. Pour le premier déploiement, et lors de modifications des models de BDD, passer la variable de `update` à `created`, dans le fichier `application.properties`, dans le backend

6. Cliquer sur l'icône `Gradle` dans la sidebar de droite et cliquer sur `Tasks > application > bootRun`



### Frontend

1. Installer Node.js (runtime JavaScript) :

https://nodejs.org/en/download/current

2. Installer PNPM (gestionnaire de paquets) :
```bash
npm install -g pnpm
```

3. Naviguer dans le dossier `frontend` :
```bash
cd frontend
```

4. Installer les dépendances :
```bash
pnpm install
```

5. Configurer le linter :

- `Paramètres > Languages & Frameworks >  JavaScript > Code Quality Tools > ESLint`
- Cocher `Automatic ESLint configuration`
- Cocher `Run eslint --fix on save`

6. Lancer le serveur de développement :
```bash
pnpm dev
```

## Login & LDAP

La connexion sur l'application se fait de 2 façon différentes. 

Dans un premier temps, il faut renseigner l'adresse mail et le nom du du chef de projet, dans le fichier backend `application.properties`, variables `app.pl.email` et `app.pl.name`. Grâce à cela, au lancement du projet (local et sur serveur), l'utilisateur principal sera crée.

- **Connexion en local** : il suffit d'avoir l'adresse mail d'un utilisateur en BDD afin de se connecter. Le chef de projet peut donc se connecter en premier, et ajouter des élèves, prof référents, coach... qui pourront également se connecter dès que leur email est en BDD, avec leurs rôles respectifs. 

- **Connexion en ligne** :  avant de déployer l'application via la [pipeline](#pipeline-gitlab), il faut configurer le LDAP dans le fichier `application.properties` dans le backend, via les variables : `spring.ldap`. [LDAP, c’est quoi ?](https://www.pingidentity.com/fr/resources/identity-fundamentals/authentication-authorization-protocols/ldap.html#:~:text=Le%20LDAP%20(Lightweight%20Directory%20Access,serveurs%20doivent%20coder%20les%20r%C3%A9ponses)). Une fois la configuration prête, et le projet déployer sur serveur, cela se passe comme en local : le chef de projet est le prmeier utilisateur. Il peut donc se connecter pour ajouter des élèves, prof référents, coach... A leur ajout, leur email est stockée en BDD, et elle doit être la même que dans le LDAP. Ces nouveaux utilisateurs peuvent à leur tour se connecter via leurs identifiants ESEO par exemple, si l'application est reliée au LDAP de l'école. 




## Pipeline Gitlab

Le projet contient une pipeline permettant un déploiement continue. Cette pipeline contient plusieurs étapes : 
  - build-frontend
  - build-backend
  - sonar
  - deploy-server
  - selenium

Chaque étape est effectuée automatiquement, comprenant le build des différentes parties, les tests et le déploiement sur serveur. Vous pouvez suivre leur avancement sur sur le Gitlab du projet, sur la page [build/pipeline](https://172.24.7.8/e4e/ld/projet-gl/2023-2024/nath/-/pipelines). Notre gitlab contient 3 branches principales : `main`, `dev` et `dev2`. La branche dev2 est celle utilisée au quotidient, afin de mettre en commun le code en cours de développement. 
- **Déploiement serveur de développement**. Il faut faire un merge de la branche `dev2` sur `dev`, et la pipeline va automatiquement s'executer, avec le build, les tests et le déploiement.  
- **Déploiement serveur de production**, Il faut faire un merge de la branche `dev` sur `main`, et la pipeline va automatiquement s'executer, avec le build, les tests et le déploiement.  


## Environnement des serveurs de dev/prod

1. Un document dans le wiki de l'application OpenProject de l'équipe Nath Tauri, explique en détail toutes les étapes et installations afin d'obtenir le même environnement serveur. Section `Serveur/Préparation serveur`

2. Ensuite, suivez les informations situées sur la section `Serveur`, de OpenProject, pour les différentes configurations à mettre en place pour déployer l'application



## Lancement des tests via Gradle

### Via IntelliJ

1. Tests unitaire : lancement via le bouton "play" sur le côté de la class test
2. Tests Selenium : commenter dans le build.gradle ligne 53 env. : `exclude "**/selenium/**"`. Rebuild le gradle via l'icon éléphant en haut à droite. Puis executer les tests via le bouton "play" dans la class de test Selenium


### Via ligne de commande 

1. Tests unitaire : gradlew test
2. Tests Selenium : gradlew seleniumTest


