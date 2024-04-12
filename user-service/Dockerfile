FROM openjdk:21-slim
COPY --from=carnotificator-project-builder /app/user-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java",  "--enable-preview", "-jar", "app.jar"]