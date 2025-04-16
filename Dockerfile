FROM maven:3.8.5-openjdk-17-slim
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn clean package -DskipTests
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "target/machines-0.0.1-SNAPSHOT.jar"]
