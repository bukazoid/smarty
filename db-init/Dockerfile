FROM openjdk:11-jre-slim
COPY /target/db-init.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]