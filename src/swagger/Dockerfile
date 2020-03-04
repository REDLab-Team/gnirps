# use a mminimalist image as root
FROM openjdk:8-jdk-alpine

# copy script to load docker-secrets as environment variables and project archive
ADD src/main/resources/load_docker-secrets_as_env-var.sh load_secrets.sh
RUN chmod +x load_secrets.sh
ADD target/api.jar app.jar

# deploy project
ENTRYPOINT /bin/sh -c ". load_secrets.sh && java -jar /app.jar"