FROM openjdk:16

COPY target/*.jar target/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/target/io-backend-0.0.1-SNAPSHOT.jar"]
