#FROM openjdk:11
FROM maven:3.8.1-adoptopenjdk-11 AS MAVEN_TOOL_CHAIN
RUN mkdir /home/spring
RUN mkdir /home/spring/.m2
RUN mkdir /home/spring/.m2/repository
COPY pom.xml /tmp/
COPY api/src/ /tmp/src/
WORKDIR /tmp/
RUN mvn package
