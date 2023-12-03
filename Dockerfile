FROM openjdk:17-alpine

WORKDIR /app

COPY target/EnglishBot-1.0.0.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
