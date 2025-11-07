# Используем официальный образ Maven для сборки проекта
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

# Используем официальный образ OpenJDK для запуска приложения
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
RUN mkdir -p /var/log
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", \
            "-XX:+UseG1GC", \
            "-XX:+PrintGCDetails", \
            "-Xlog:gc,safepoint:/var/log/gc.log::filecount=5,filesize=20M", \
            "-jar", "app.jar"]
