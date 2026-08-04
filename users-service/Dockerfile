FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew

RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8787

ENTRYPOINT ["java","-jar","app.jar"]