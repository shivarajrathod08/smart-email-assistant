# Stage 1: Build jar with Maven + Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Download Maven dependencies (cacheable layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar, skip tests
RUN mvn clean package -DskipTests

# Stage 2: Run app with Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/SmartDraft-0.0.1-SNAPSHOT.jar app.jar

# Expose Render default port
EXPOSE 10000

# Start the app using Render's $PORT
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]"]