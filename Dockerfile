# syntax=docker/dockerfile:1

FROM openjdk:11
COPY . ./
RUN ./gradlew build && mv ./build/libs/*-runner.jar ./app.jar
CMD ["./gradlew", "run"]
