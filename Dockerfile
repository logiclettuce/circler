FROM openjdk:18
WORKDIR /var/app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]