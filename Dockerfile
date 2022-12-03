FROM maven:3.8-openjdk-17 as builder
COPY . /app
WORKDIR /app
RUN mvn install

FROM openjdk:17
WORKDIR /
COPY --from=builder /app/target/racing-arena-server.jar /
EXPOSE 6969
ENTRYPOINT ["java", "-jar", "racing-arena-server.jar"]