# Etapa de construcción usando Gradle
FROM gradle:8.6.0-jdk21-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/build/libs/franquicias-api-0.0.1-SNAPSHOT.jar app.jar

# Configuración de la JVM para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
