# Stage 1: Build the jar using Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar, skip tests for faster build
RUN mvn clean package -DskipTests

# Stage 2: Run the app with a lightweight JDK
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/SmartDraft-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot uses
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]