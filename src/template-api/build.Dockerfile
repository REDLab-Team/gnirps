FROM openjdk:15-alpine

# install bash
RUN ["apk", "add", "bash"]

# install maven
RUN apk add --update ca-certificates && rm -rf /var/cache/apk/* && \
  find /usr/share/ca-certificates/mozilla/ -name "*.crt" -exec keytool -import -trustcacerts \
  -keystore /opt/openjdk-15/lib/security/cacerts -storepass changeit -noprompt \
  -file {} -alias {} \; && \
  keytool -list -keystore /opt/openjdk-15/lib/security/cacerts --storepass changeit
ENV MAVEN_VERSION 3.6.3
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH
RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

# install and run docker
CMD ["apk", "add", "docker"]
CMD ["apk", "update"]
CMD ["rc-update", "add", "docker", "boot"]
CMD ["service", "docker", "start"]

# copy project files
WORKDIR /project
ADD pom.xml /project
ADD Dockerfile /project
ADD src src
ADD bin bin
RUN mv bin/docker/settings.xml /usr/lib/mvn/conf/settings.xml

CMD ["/bin/bash", "-c", "bin/build_locally.sh $ARGS"]