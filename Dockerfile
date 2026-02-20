
# Use an official Maven image as the base image
FROM maven:3.9.9-eclipse-temurin-23 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src

# Run Maven build to download dependencies and package the application
RUN mvn clean package -DskipTests

# Stage 2: Final image for running the application
FROM eclipse-temurin:23-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged application from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entry point for running the application
ENTRYPOINT ["java", "-jar", "app.jar"]