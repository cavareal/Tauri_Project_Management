FROM gradle:8.4-jdk17

USER root

# Install GnuPG
RUN apt-get update && apt-get install -y gnupg
RUN apt-get update && apt-get install -y sudo

# Install ChromeDriver
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
RUN apt-get update && apt-get install -y google-chrome-stable
RUN CHROMEDRIVER_VERSION=$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -q "https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip" && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/local/bin && \
    rm chromedriver_linux64.zip

WORKDIR /home/gradle/project
RUN apt-get update && apt-get install -y sudo

CMD ["gradle", "--version"]
