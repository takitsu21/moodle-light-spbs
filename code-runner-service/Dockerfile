FROM openjdk:11
# command to run when building
RUN groupadd -r coderunner && useradd -r coderunner -g coderunner
# user name to run the image
USER coderunner:coderunner
# build time variable (with default value here)
ARG JAR_FILE=code-runner-service/final-jar/*.jar
# copy file from host to container
COPY ${JAR_FILE} app.jar
# command to run at start
ENTRYPOINT java -jar /app.jar