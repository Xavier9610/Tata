FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml pom.xml
COPY client-service/pom.xml client-service/pom.xml
COPY account-service/pom.xml account-service/pom.xml
RUN mvn -q -pl client-service dependency:go-offline
COPY client-service/src client-service/src
RUN mvn -q -pl client-service package -DskipTests
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /workspace/client-service/target/client-service-1.0.0.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
