FROM amazoncorretto:21-alpine-jdk
LABEL authors="mischok academy"
COPY target/todoapp-0.0.1-SNAPSHOT.jar todoapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/todoapp.jar"]