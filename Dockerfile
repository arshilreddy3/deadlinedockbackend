# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and configuration
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Ensure Linux line endings and execution permission for mvnw
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build production jar
COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Run as non-privileged spring user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
