# Stage 1: Build jar with Maven + Java 21 (used only for reference; already built locally if desired)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# If you want Render to build jar, uncomment these lines
# COPY .mvn/ .mvn/
# COPY mvnw pom.xml ./
# RUN ./mvnw clean package -DskipTests
# COPY src ./src
# RUN ./mvnw clean package -DskipTests

# Stage 2: Run app
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy your locally built jar
COPY target/SmartDraft-0.0.1-SNAPSHOT.jar app.jar

# Expose Render port
EXPOSE 10000

# Run Spring Boot using Render's $PORT
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]