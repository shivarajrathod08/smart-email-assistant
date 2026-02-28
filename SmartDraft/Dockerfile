# Stage 1: Build jar with Maven + Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run app with Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/SmartDraft-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

# Safe ENTRYPOINT without shell quoting issues
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]