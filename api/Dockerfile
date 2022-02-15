#FROM openjdk:11
## command to run when building
#RUN groupadd -r api && useradd -r api -g api
## user name to run the image
#USER api:api
## build time variable (with default value here)
##ARG JAR_FILE=./target/*.jar
## copy file from host to container
#COPY . /api

FROM openjdk:11
# command to run when building
RUN groupadd -r api && useradd -r api -g api
# user name to run the image
USER api:api
# build time variable (with default value here)
ARG JAR_FILE=api/final-jar/*.jar
# copy file from host to container
COPY ${JAR_FILE} app.jar
# command to run at start
ENTRYPOINT java -jar /app.jar