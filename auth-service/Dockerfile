FROM openjdk:11
# command to run when building
RUN groupadd -r auth && useradd -r auth -g auth
# user name to run the image
USER auth:auth
# build time variable (with default value here)
ARG JAR_FILE=./target/*.jar
# copy file from host to container
COPY ${JAR_FILE} app.jar
# command to run at start
ENTRYPOINT java -jar /app.jar