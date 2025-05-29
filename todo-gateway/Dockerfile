FROM openjdk:17-jdk-slim
LABEL maintainer="guoweijin500@gmail.com"
WORKDIR /app
COPY target/todo-gateway-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]