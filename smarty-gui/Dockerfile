FROM openjdk:11-jre-slim
COPY /target/smarty-gui.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]