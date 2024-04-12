FROM openjdk:21-slim
COPY --from=carnotificator-project-builder /app/observer-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java",  "--enable-preview", "-jar", "app.jar"]