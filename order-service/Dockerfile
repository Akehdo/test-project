FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY order-service ./order-service
COPY notification-service/build.gradle.kts ./notification-service/build.gradle.kts

RUN ./gradlew :order-service:bootJar --no-daemon


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/order-service/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]