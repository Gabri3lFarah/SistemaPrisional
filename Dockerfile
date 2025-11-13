####
# This Dockerfile is used to build the prisioneiro-core container image.
# It uses a multi-stage build to optimize the final image size.
####

## Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Configure Maven to skip SSL certificate validation (for build environments with cert issues)
ENV MAVEN_OPTS="-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true"

# Copy project files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

## Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for running the application
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus

# Copy the built JAR from the build stage
COPY --from=build /app/target/quarkus-app/lib/ /app/lib/
COPY --from=build /app/target/quarkus-app/*.jar /app/
COPY --from=build /app/target/quarkus-app/app/ /app/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /app/quarkus/

# Change ownership to the non-root user
RUN chown -R quarkus:quarkus /app

USER quarkus

# Expose the application port
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]
