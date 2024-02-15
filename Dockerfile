FROM --platform=linux/amd64 openjdk:21-jdk
VOLUME /tmp
EXPOSE 8080
COPY target/AppliedCloudCW1*.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]