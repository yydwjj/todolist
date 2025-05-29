FROM openjdk:17-jdk-slim
LABEL maintainer="guoweijin500@gmail.com"
WORKDIR /app
COPY target/todo-PageService-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]