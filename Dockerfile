FROM clojure:tools-deps-bookworm-slim

ENV DISPLAY :99.0
ENV SO_DATA_DIR /tmp/data

RUN apt update && apt install -y xvfb fluxbox wget gpg firefox-esr

RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz &&\
    wget https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz.asc &&\
    gpg --recv-keys 14F26682D0916CDD81E37B6D61B7B526D98F0353 &&\
    gpg --verify geckodriver-v0.34.0-linux64.tar.gz.asc &&\
    tar xvzf geckodriver-v0.34.0-linux64.tar.gz && mv geckodriver /usr/local/bin/ && chmod +x /usr/local/bin/geckodriver

ADD . .

CMD Xvfb :99 -screen 0 1024x768x24 &&\
    fluxbox -display :99 &&\
    clojure -X core/-main
