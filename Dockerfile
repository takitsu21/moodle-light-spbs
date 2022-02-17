#FROM openjdk:11
FROM maven:3.8.1-adoptopenjdk-11
USER root
RUN mkdir -p /home/spring/.m2/repository
WORKDIR /app
COPY . .
ENTRYPOINT tail -f /dev/null