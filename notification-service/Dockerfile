FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY notification-service ./notification-service
COPY order-service/build.gradle.kts ./order-service/build.gradle.kts

RUN ./gradlew :notification-service:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/notification-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]