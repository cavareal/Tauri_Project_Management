# Tauri

## Présentation

Ce projet a pour but de développer une application afin de permettre une gestion de projet, des équipes, des notes... pour la matière Projet Génie Logiciel !



## Lancement de l'application en local

### BDD

1. Installer MariaDB ou MYSQL sur sa machine, sur le port 3306 de préférence. Sinon, modifier l'adresse de la BDD dans le fichier `backend/src/main/resources/application/properties`, variable `spring.datasource.url`

### Backend, avec Intellij

1. Installer le JDK 17 :

https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

2. Configurer le projet :

- `File > Project Structure > Project`
- Choisir le JDK 17
- Choisir le niveau de langage `SDK default`

3. Cliquer sur `Import Gradle Project` dans la fenêtre de notification

4. Copier/coller le fichier `backend/src/main/resources/.env.example` et renomer en `.env`. Puis ajouter l'utilisateur et mot de passe de la BDD.

5. Renseigner l'adresse mail et le nom du chef de projet, dans le fichier backend `application.properties`, variables `app.pl.email` et `app.pl.name`. Grâce à cela, au lancement du projet (en local et sur serveur), le compte du chef de projet sera automatiquement crée.

6. Pour le premier déploiement, et lors de modifications des models de BDD, passer la variable `spring.jpa.hibernate.ddl-auto` de `update` à `created`, dans le fichier `application.properties` du backend

7. Cliquer sur l'icône `Gradle` dans la sidebar de droite et cliquer sur `Tasks > application > bootRun`

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



## Connexion à l'application

Une fois le frontend et le backend lancés, l'application tourne sur le port 5173 pour le front, et 8882 pour le back. Ensuite la connexion sur l'application se fait de 2 façon différentes. Vous pouvez donc accéder à l'interface utilisateur via l'adresse http://localhost:5173/

- **Connexion en local** : il suffit d'avoir l'adresse mail d'un utilisateur en BDD afin de se connecter. Le mot de passe n'est **pas vérifié en local** pour aucun utilisateurs, afin de simplifier le développement. Il faut quand même remplir la zone de texte du mot de passe. Le chef de projet peut donc se connecter en premier, et ajouter des élèves, prof référents, coach... qui pourront ensuite se connecter avec leur adresse mail, et auront leurs rôles respectifs. 

- **Connexion en ligne** :  avant de déployer l'application via la [pipeline](#pipeline-gitlab), il faut [préparer les serveurs](#environnement-des-serveurs-de-devprod), puis [configurer le LDAP](#configuration). Une fois la configuration prête, et le projet déployer sur serveur, cela se passe comme en local : le chef de projet est le premier utilisateur. Il peut donc se connecter avec son mail et mot de passe enregistrée sur le LDAP. Ensuite il peut ajouter des élèves, prof référents, coach... A leur ajout, leur email est stockée en BDD, et elle doit être la même que dans le LDAP. Ces nouveaux utilisateurs peuvent à leur tour se connecter via leurs identifiants ESEO par exemple, si l'application est reliée au LDAP de l'école. 



## Lancement des tests via Gradle

### Via IntelliJ

1. Tests unitaire : lancement via le bouton "play" sur le côté de la class test
2. Tests Selenium : commenter dans le build.gradle ligne 53 env. : `exclude "**/selenium/**"`. Rebuild le gradle via l'icon éléphant en haut à droite. Puis executer les tests via le bouton "play" dans la class de test Selenium

### Via ligne de commande 

Depuis le dossier `backend`
1. Tests unitaire : gradlew test
2. Tests Selenium : gradlew seleniumTest



## Pipeline Gitlab

Le projet contient une pipeline (fichier .gitlab-ci.yml) permettant un déploiement continue. Cette pipeline contient plusieurs étapes : 
  - build-frontend
  - build-backend
  - sonar
  - deploy-server
  - selenium

Chaque étapes est effectuées automatiquement, comprenant le build des différentes parties, les tests et le déploiement sur serveur. Vous pouvez suivre leur avancement sur le Gitlab du projet, page [build/pipeline](https://172.24.7.8/e4e/ld/projet-gl/2023-2024/nath/-/pipelines). Notre gitlab contient 3 branches principales : `main`, `dev` et `dev2`. La branche `dev2` est celle utilisée au quotidient, afin de mettre en commun le code en cours de développement. 

- La branche `dev` permet de déployer sur le serveur de **développement**. Il suffit de merge `dev2` sur `dev`, et la pipeline va automatiquement s'executer

- La branche `main` permet de déployer sur le serveur de **production**. Il suffit de merge `dev` sur `main`, et la pipeline va automatiquement s'executer, avec le build, les tests et le déploiement.  

Les étapes de la pipeline se déclenchent automatiquement en fonction de la branche sur laquelle le code est push. Cela permet de gérer les différents paramètres entre serveur de développement et production. 



## LDAP

### Configuration

[LDAP, c’est quoi ?](https://www.pingidentity.com/fr/resources/identity-fundamentals/authentication-authorization-protocols/ldap.html#:~:text=Le%20LDAP%20(Lightweight%20Directory%20Access,serveurs%20doivent%20coder%20les%20r%C3%A9ponses)). 

Dans le fichier `application.properties` dans le backend, il faut modifier les variables commençant par : `spring.ldap.` :
- `url` : adresse du serveur LDAP auquel on souhaite se connecter
- `base-dn` : base du nom de domaine du serveur
- `username` : utilisateur pour se connecter au serveur LDAP
- `password` : mot de passe de l'utilisateur
- `user-search-filter` : filtre à vérifier lors de la connexion. Ici c'est l'adresse mail, cette valeur ne doit donc pas être changée

### Ajouter un utilisateur 

**Prérequis :** [environnements serveurs](#environnement-des-serveurs-de-devprod) mis en places.
Se connecter sur la page de LAM (Ldap Account Manager), à l'adresse `https://{ip-serveur}/lam/templates/login.php`. 
L'ajout d'utilisateurs est détaillée sur le wiki, section `Serveur/Préparation serveur/Ajout d'utilisateurs LDAP`

### Modifier le type de hashage d'un mot de passe

Il faut se rendre sur la page LAM, onglet Tools/Tree view. Puis déplier la liste "ou=user". Pour chaque utilisateur, on peut choisir le hashage souhaité pour son mot de passe. 


## Environnement des serveurs de dev/prod

Sur le wiki de l'application OpenProject de l'équipe Nath Tauri, un onglet `Serveur` explique toutes les étapes d'installation et de configuration pour préparer le serveur au déploiement de l'application

