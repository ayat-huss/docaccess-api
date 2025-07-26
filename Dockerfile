# Dockerfile

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from Maven build
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
