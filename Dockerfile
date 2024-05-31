# Utiliser une image de base avec Gradle 8.4 et JDK 17
FROM gradle:8.4.0-jdk17

# Installer les dépendances nécessaires pour Chrome et ChromeDriver
RUN apt-get update && apt-get install -y wget gnupg2 unzip


RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
RUN apt-get update && apt-get install -y google-chrome-stable


# Télécharger et installer ChromeDriver
ENV CHROME_DRIVER_VERSION=114.0.5735.90
RUN wget -N https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm chromedriver_linux64.zip
    
ENV SELENIUM_VERSION=4.19.1

RUN apt-get install -y libnss3 libgconf-2-4


