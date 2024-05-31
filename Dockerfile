# # Utiliser l'image de base selenium/standalone-chrome
# FROM selenium/standalone-chrome

# USER root
# # Installer les dépendances nécessaires pour Gradle
# RUN apt-get update && apt-get install -y wget unzip

# # Télécharger et installer Gradle
# ARG GRADLE_VERSION=8.4
# RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp \
#     && unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /opt \
#     && ln -s /opt/gradle-${GRADLE_VERSION} /opt/gradle \
#     && rm /tmp/gradle-${GRADLE_VERSION}-bin.zip

# # Mettre à jour la variable d'environnement PATH
# ENV GRADLE_HOME=/opt/gradle
# ENV PATH=${GRADLE_HOME}/bin:${PATH}

# # Vérifier l'installation de Gradle
# RUN gradle --version

# MRC : https://github.com/markhobson/docker-maven-chrome


# Utiliser une image de base avec Gradle 8.4 et JDK 17
FROM gradle:8.4.0-jdk17

# Installer les dépendances nécessaires pour Chrome et ChromeDriver
RUN apt-get update && apt-get install -y wget gnupg2 libnss3 libgconf-2-4

# Google Chrome
ARG CHROME_VERSION=125.0.6422.141-1
RUN apt-get update -qqy \
    && apt-get -qqy install gpg unzip \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update -qqy \
    && apt-get -qqy install google-chrome-stable=$CHROME_VERSION \
    && rm /etc/apt/sources.list.d/google-chrome.list \
    && rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
    && sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# ChromeDriver
ARG CHROME_DRIVER_VERSION=125.0.6422.141
RUN wget -q -O /tmp/chromedriver.zip https://storage.googleapis.com/chrome-for-testing-public/$CHROME_DRIVER_VERSION/linux64/chromedriver-linux64.zip \
    && unzip /tmp/chromedriver.zip -d /opt \
    && rm /tmp/chromedriver.zip \
    && ln -s /opt/chromedriver-linux64/chromedriver /usr/bin/chromedriver


