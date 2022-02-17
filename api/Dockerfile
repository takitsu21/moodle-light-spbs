#FROM openjdk:11
FROM maven:3.8.1-adoptopenjdk-11
# command to run when building

# user name to run the image
USER api:api
# build time variable (with default value here)
ARG JAR_FILE=api/final-jar/*.jar
# copy file from host to container
COPY . /
# command to run at start
ENTRYPOINT mvn test