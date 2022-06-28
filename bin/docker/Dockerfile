FROM maven:3.8.5-eclipse-temurin-16

# install docker
RUN curl -fsSL https://get.docker.com -o get-docker.sh
RUN DRY_RUN=1 sh ./get-docker.sh

# copy project files
WORKDIR /project
ADD . .
RUN mkdir $MAVEN_CONFIG
RUN mv bin/docker/settings.xml $MAVEN_CONFIG/settings.xml

# build in container
CMD ["/bin/bash", "-c", "bin/build_locally.sh $ARGS"]
