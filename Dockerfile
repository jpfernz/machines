# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Cache deps first
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -q -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Spring Boot exposes 8082 per application.yml
EXPOSE 8082

# Copy fat jar
COPY --from=build /workspace/target/*.jar /app/app.jar

# Run
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
