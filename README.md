# Tauri

## Setup

### Backend

1. Installer le JDK 17 :

https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

2. Configurer le projet :

- `File > Project Structure > Project`
- Choisir le JDK 17
- Choisir le niveau de langage `SDK default`

3. Cliquer sur `Import Gradle Project` dans la fenêtre de notification

4. Modifier dans le fichier "application.properties" : datasource.url, datasource.username et datasource.password

4. Cliquer sur l'icône `Gradle` dans la sidebar de droite et cliquer sur `Tasks > application > bootRun`



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


## Lancement des testes backend

### Via IntelliJ

1. Testes unitaire : lancement via le bouton "play" sur le côté de la class test
2. Testes Selenium : commenter dans le build.gradle ligne 53 env. : `exclude "**/selenium/**"`. Rebuild le gradle via l'icon éléphant en haut à droite. Puis executer les tests via le bouton "play" dans la class de test Selenium


### Via ligne de commande 

1. Testes unitaire : gradlew test
2. Testes Selenium : gradlew seleniumTest