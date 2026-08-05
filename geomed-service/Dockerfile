FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew bootJar -x test --no-daemon


RUN find build/libs/ -name "*.jar" ! -name "*-plain.jar" -exec cp {} /app/app.jar \;

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia el archivo estático generado en la etapa de build
COPY --from=build /app/app.jar app.jar

EXPOSE 8788

ENTRYPOINT ["java", "-jar", "app.jar"]