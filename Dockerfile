FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/flowcrmtutorial-1.0-SNAPSHOT.jar flowcrmtutorial.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "flowcrmtutorial.jar"]


