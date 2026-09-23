FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml pom.xml
COPY account-service/pom.xml account-service/pom.xml
COPY client-service/pom.xml client-service/pom.xml
RUN mvn -q -pl account-service dependency:go-offline
COPY account-service/src account-service/src
RUN mvn -q -pl account-service package -DskipTests
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /workspace/account-service/target/account-service-1.0.0.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
