# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .
# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Define build arguments
ARG SPRING_DB_DRIVE
ARG SPRING_DB_URL
ARG SPRING_DB_USER
ARG SPRING_DB_PASSWORD
ARG SPRING_DB_SCHEMA

# Set environment variables for the build
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=${SPRING_DB_DRIVE}
ENV SPRING_DATASOURCE_URL=${SPRING_DB_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DB_USER}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DB_PASSWORD}
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA=${SPRING_DB_SCHEMA}

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (updated to match your Dockerfile)
EXPOSE 8888

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
