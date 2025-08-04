# Use a base image with Java
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy everything into container
COPY . .

# Build the app (assumes you use Maven)
RUN ./mvnw clean install -DskipTests

# Expose port (Spring Boot default)
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/*.jar"]
