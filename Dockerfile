FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} drools-sample2.jar
ENTRYPOINT ["java","-jar","/drools-sample2.jar"]