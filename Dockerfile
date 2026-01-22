# Build stage
FROM gradle:8.5.0-jdk21 AS build

WORKDIR /app

# Copy gradle files and download dependencies
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# Copy source code
COPY src ./src

# Build the application
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Create non-root user
RUN addgroup --system appuser && adduser --system --group appuser

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to appuser
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
