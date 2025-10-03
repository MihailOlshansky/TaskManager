FROM openjdk:17-jdk-slim
COPY /src /src
COPY pom.xml /
CMD mvn -f /pom.xml clean package

COPY /target/*.jar application.jar
EXPOSE 8080 4200
ENTRYPOINT ["java", "-jar", "application.jar"]