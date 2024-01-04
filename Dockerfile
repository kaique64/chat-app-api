FROM openjdk:17-jdk-slim
EXPOSE 8080
ARG JAR_FILE=target/app-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]