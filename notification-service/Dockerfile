# Stage 1: build the Spring Boot jar.
# We use a JDK image here because Gradle needs a compiler to build the project.
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# These files live in the project root. Docker Compose uses `context: .`,
# so the Dockerfile can copy files from the root even though the Dockerfile
# itself is stored in `notification-service/`.
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

# Copy the full source of the service that this image actually builds.
COPY notification-service ./notification-service

# The root `settings.gradle.kts` includes both modules:
#   include("order-service")
#   include("notification-service")
# Gradle configures all included modules before running the requested task.
# Because of that, the sibling module's build file must exist in the Docker
# build context. We copy only its `build.gradle.kts`, not its source, because
# this image does not build order-service.
COPY order-service/build.gradle.kts ./order-service/build.gradle.kts
COPY user-service/build.gradle.kts ./user-service/build.gradle.kts

# Build only notification-service. The leading colon means "run this task for
# this Gradle module", not for the whole root project.
RUN ./gradlew :notification-service:bootJar -x test --no-daemon


# Stage 2: runtime image.
# We use a smaller JRE image here because the final container only runs the jar.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built jar from the build stage. Gradle, source files, and build
# caches stay behind and do not get into the final image.
COPY --from=build /app/notification-service/build/libs/*.jar app.jar

# This documents the port used by the app. The actual host mapping is configured
# in docker-compose.yml with `ports:`.
EXPOSE 8080

# Production containers should run the built artifact, not `gradlew bootRun`.
ENTRYPOINT ["java", "-jar", "app.jar"]
