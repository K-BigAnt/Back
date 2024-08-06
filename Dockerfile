FROM openjdk:17

COPY ./build/libs/*.jar app.jar
COPY Back-Secret/application-dev.yml application.yml

ENTRYPOINT ["java","-jar","/app.jar"]