FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar shophub-server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/shophub-server.jar"]
