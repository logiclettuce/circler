FROM openjdk:18
ARG JAR_FILE=build/libs/circler-ALPHA.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]