FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Garantiza copiar únicamente el JAR ejecutable de Spring Boot
COPY --from=build /app/build/libs/*[!plain].jar app.jar

EXPOSE 8788

ENTRYPOINT ["java", "-jar", "app.jar"]