# Dockerfile
FROM eclipse-temurin:21-jdk-jammy

ARG JAR_FILE=build/libs/franquicias-api-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
