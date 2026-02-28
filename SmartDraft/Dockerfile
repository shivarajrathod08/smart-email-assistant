# Stage 1: Build jar with Maven Wrapper + Java 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven Wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (cacheable layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar
RUN ./mvnw clean package -DskipTests

# Stage 2: Run app
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/SmartDraft-0.0.1-SNAPSHOT.jar app.jar

# Expose Render port
EXPOSE 10000

# Run Spring Boot on Render's port
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]