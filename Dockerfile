# Dockerfile
# Build stage
FROM openjdk:8-jre-alpine
FROM maven:3.6.2-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn install

# package stage
EXPOSE 8080
WORKDIR /home/app
#ADD "target/ws-crawler-api-0.0.1-SNAPSHOT.jar" "app/ws-crawler-api-0.0.1-SNAPSHOT.jar"
ENTRYPOINT ["java", "-jar", "target/ws-crawler-api-0.0.1-SNAPSHOT.jar"]
