FROM openjdk:21-slim
COPY --from=carnotificator-project-builder /app/notification-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java",  "--enable-preview", "-jar", "app.jar"]