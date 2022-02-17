#FROM openjdk:11
FROM maven:3.8.1-adoptopenjdk-11 AS MAVEN_TOOL_CHAIN
USER root
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/postgres
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=123
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
#RUN mkdir -p /home/spring
#RUN chown api /home/spring
#RUN mkdir -p /home/spring/.m2
RUN mkdir -p /home/spring/.m2/repository
WORKDIR /app
COPY . .
#RUN mvn package
