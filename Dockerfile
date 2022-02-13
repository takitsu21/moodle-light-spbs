FROM openjdk:11
# command to run when building
RUN groupadd -r spring && useradd -r spring -g spring
# user name to run the image
USER spring:spring
# build time variable (with default value here)
EXPOSE 8080
ARG JAR_FILE=target/*.jar
# copy file from host to container
COPY ${JAR_FILE} app.jar
# command to run at start
ENTRYPOINT java -jar /app.jar