FROM eclipse-temurin:16-jdk

# copy script to load docker-secrets as environment variables and project archive
ADD src/main/resources/load_docker-secrets_as_env-var.sh load_secrets.sh
RUN chmod +x load_secrets.sh
ADD target/api.jar app.jar

# deploy project
CMD ["/bin/sh", "-c", ". load_secrets.sh && java -jar /app.jar"]