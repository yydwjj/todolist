FROM openjdk:17-jdk-slim
#FROM gcr.io/distroless/java-debian12
LABEL maintainer="guoweijin500@gmail.com"
WORKDIR /app
COPY target/todo-UserService-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]