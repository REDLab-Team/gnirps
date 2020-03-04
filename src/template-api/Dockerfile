# use a mminimalist image as root
FROM openjdk:8-jdk-alpine

# copy script to load docker-secrets as environment variables and project archive
COPY src/main/resources/load_docker-secrets_as_env-var.sh load_secrets.sh
COPY target/api.jar app.jar
#RUN ["/bin/sh", ".", "load_secrets.sh"]

# deploy project
CMD ["/bin/sh", "-c", ". load_secrets.sh && java -jar /app.jar"]
