FROM openjdk:17

COPY ./build/libs/*.jar app.jar
COPY src/main/resources/application-dev.yml application.yml

ENTRYPOINT ["java","-jar","/app.jar"]