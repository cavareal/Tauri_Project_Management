# Utiliser l'image de base selenium/standalone-chrome
FROM selenium/standalone-chrome

USER root
# Installer les dépendances nécessaires pour Gradle
RUN apt-get update && apt-get install -y wget unzip

# Télécharger et installer Gradle
ARG GRADLE_VERSION=8.4
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp \
    && unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /opt \
    && ln -s /opt/gradle-${GRADLE_VERSION} /opt/gradle \
    && rm /tmp/gradle-${GRADLE_VERSION}-bin.zip

# Mettre à jour la variable d'environnement PATH
ENV GRADLE_HOME=/opt/gradle
ENV PATH=${GRADLE_HOME}/bin:${PATH}

# Vérifier l'installation de Gradle
RUN gradle --version

# MRC : https://github.com/markhobson/docker-maven-chrome