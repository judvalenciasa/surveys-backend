# Dockerfile
FROM eclipse-temurin:17-jre-alpine

# Crear directorio de la aplicación
WORKDIR /app

# Copiar el jar de la aplicación
COPY target/surveys-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8082

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=aws

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]