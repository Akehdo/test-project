FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY user-service ./user-service
COPY order-service/build.gradle.kts ./order-service/build.gradle.kts
COPY notification-service/build.gradle.kts ./notification-service/build.gradle.kts
COPY proto-contract/build.gradle.kts ./proto-contract/build.gradle.kts
COPY proto-contract/src ./proto-contract/src

RUN ./gradlew :user-service:bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/user-service/build/libs/*.jar app.jar

EXPOSE 8082 9090

ENTRYPOINT ["java", "-jar", "app.jar"]
