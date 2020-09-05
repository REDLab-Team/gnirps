                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                                                                  
                       __     _____       _ __       __                     __                     _            
                  ____/ /__  / __(_)___  (_) /____  / /_  __   ____  ____  / /_   _________  _____(_)___  ____ _
                 / __  / _ \/ /_/ / __ \/ / __/ _ \/ / / / /  / __ \/ __ \/ __/  / ___/ __ \/ ___/ / __ \/ __ `/
                / /_/ /  __/ __/ / / / / / /_/  __/ / /_/ /  / / / / /_/ / /_   (__  ) /_/ / /  / / / / / /_/ / 
                \__,_/\___/_/ /_/_/ /_/_/\__/\___/_/\__, /  /_/ /_/\____/\__/  /____/ .___/_/  /_/_/ /_/\__, /  
                                                   /____/                          /_/                 /____/   

## Summary

This project can be seen as a framework, bringing together and expanding several tools in Kotlin:
* [SpringBoot](https://spring.io/projects/spring-boot) - Rest API framework
* [Maven](https://maven.apache.org/) - Build tool
* [Swagger](https://swagger.io/) - Documentation generator
* [Docker](https://www.docker.com/) - Container platform
* [Nexus](https://www.sonatype.com/nexus-repository-sonatype/) - Dependencies repository

### Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Usage](#usage)
    1. [With Maven and Docker](#with-maven-and-docker)
    2. [With Docker alone (remote sources)](#with-docker-alone-remote-sources)
    3. [With Docker alone (local sources)](#with-docker-alone-local-sources)
    4. [With Docker-Compose (local or remote sources)](#with-docker--compose-local-or-remote-sources)
4. [Standard project's (layered) architecture](#standard-projects-layered-architecture)
5. [Nexus Repository](#nexus-repository)
    1. [Deployment](#deployment)
    2. [Nexus usage](#nexus-usage)
6. [License](#license)

## Features

The framework is composed of several modules as Maven projects, each of them inheriting from the same parent which is 
at the root of the project. Each module and its features are documented in their own **README** file.

* The parent project, called ***gnirps-origin***, is meant to:
    * regroup the potential dependencies versions in a single place.
    * regroup some common dependencies which will be inherited by all children.
    * regroup some common plugins configurations that can be included in any child.
    * allow an easier build-and-deploy on a Maven repository of the whole package.
- **[acme](https://github.com/REDLab-Team/gnirps/tree/master/src/acme)**: 
allows the use of HTTPS freely thanks to Let's Encrypt auto-generated and validated SSL certificates.
    - [Properties](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#properties)
    - [SSL certificates generated at startup](
    https://github.com/REDLab-Team/gnirps/tree/master/src/acme#ssl-certificates-generated-at-startup)
    - [Http Challenge](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#http-challenge)
    - [LE Terms of service](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#le-terms-of-service)
- **[bash](https://github.com/REDLab-Team/gnirps/tree/master/src/bash)**: 
offers an easy way to interact with bash scripts (or python, or any script, through bash).
    - [Parallel or sequential execution](
    https://github.com/REDLab-Team/gnirps/tree/master/src/bash#parallel-or-sequential-execution)
    - [Input, error and output redirecting](
    https://github.com/REDLab-Team/gnirps/tree/master/src/bash#input,-error,-and-output-redirection)
    - [Config and monitoring properties](
    https://github.com/REDLab-Team/gnirps/tree/master/src/bash#com.gnirps.keycloak.config-and-monitoring-properties)
- **[commons](https://github.com/REDLab-Team/gnirps/tree/master/src/commons)**: 
regroups some common parts of other projects to avoid code redundancy and a heavier management of
dependencies.
    - [Properties sourcing](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#properties-sourcing)
    - [Loggers injection](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#loggers-injection)
    - [Centralised definitions](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#centralised-definitions):
        - [Errors and exceptions](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#errors-and-exceptions)
        - [Models and dtos](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#models-and-dtos)
        - [PageRequest](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#pagerequest)
        - [ExecutionTimer](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#executiontimer)
- **[postgresql-database](https://github.com/REDLab-Team/gnirps/tree/master/src/postgresql-database)**: 
offers a basic configuration to connect to a secured PostgreSQL database with custom 
naming strategies.
    - [Implementation](https://github.com/REDLab-Team/gnirps/tree/master/src/postgresql-database#implementation)
    - [Properties](https://github.com/REDLab-Team/gnirps/tree/master/src/postgresql-database#properties)
- **[swagger](https://github.com/REDLab-Team/gnirps/tree/master/src/swagger)**: 
allows the creation of a spring API, integrating a Swagger documentation and a Docker support through maven.
    - [SpringBoot](https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#springboot)
    - [Docker secrets](https://github.com/REDLab-Team/gnirps/tree/master/src/swaggerdocker-secrets)
    - [Swagger](https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#swagger)
    - [Cross Origin Request Sharing](
    https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#cross-origin-request-sharing)
    - [Errors and Exceptions Interceptor](
    https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#errors-and-exceptions-interceptor)
    - [Logger Interceptor](https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#logger-interceptor)
    - [Root redirection to documentation](
    https://github.com/REDLab-Team/gnirps/tree/master/src/swagger#root-redirection-to-documentation)
- **[template-api](https://github.com/REDLab-Team/gnirps/tree/master/src/template-api)**: an empty shell meant to be 
used as a starting point.
    - [Application settings](https://github.com/REDLab-Team/gnirps/tree/master/src/template-api#application-settings)
    - [Docker settings](https://github.com/REDLab-Team/gnirps/tree/master/src/template-api#docker-settings)

A working example can be found at **[cat-api](https://github.com/REDLab-Team/gnirps/tree/master/src/cat-api)**: 
both a POC and an example of how to put together an API with this framework, it demonstrates the use of several tools 
such as Swagger and PostgreSQL.


## Prerequisites

* ***Maven***:
    * To build locally.
* ***Docker***:
    * To build in a container without needing **Maven** on the host machine.
    * To build a local image.
    * To pull a remote image from DockerHub.
    * To deploy an image.
    * To allow the use of secrets through a stack.


## Usage

The following sections describe how to build a project with Maven and, for the suitable projects, deploy a Spring-Boot 
API through Maven or Docker.

### With Maven and Docker

* ***Build*** a war file and a docker image: 
    ```
    bash bin/build_locally.sh [module] [-d [modules-with-docker-image]]
    ```
* ***Run*** the application: 
    ```
    mvn spring-boot:run
    ```
    ou 
    ```
    bash bin/run.sh
    ```

### With Docker alone (remote sources)

* ***Login***:
    ```
    docker login
    ```
* ***Pull*** and ***run*** the image from DockerHub: 
    ```
    docker run pittinic/gnirps:kotlin-api
    ```

### With Docker alone (local sources)

* ***Build*** a war file and a local docker image, then run it: 
    ```
    bash bin/build_in_docker.sh [module] [-d [modules-with-docker-image]]
    ```
    ```
    bash bin/run.sh
    ```
* ***Pull*** and ***run*** the image from DockerHub: 
    ```
    bash bin/run.sh
    ```

## Standard project's (layered) architecture

    ├── src
    │   │
    │   ├── main
    │   │   ├── java
    │   │   ├── kotlin
    │   │   │   └─ com.gnirps.project-package
    │   │   │      ├── com.gnirps.keycloak.config ........................... configuration
    │   │   │      ├── controller ....................... endpoints
    │   │   │      ├── dto .............................. data transfer objects (reading and writing)
    │   │   │      ├── exception ........................ custom exceptions
    │   │   │      ├── mapper ........................... dto to/from model converter 
    │   │   │      ├── model ............................ core logic's units definition
    │   │   │      ├── repository ....................... interface between API and persistence unit
    │   │   │      ├── service .......................... core logic's operations definition
    │   │   │      └── SpringBootEntryPoint.kt .......... SpringBoot entrypoint
    │   │   └── resources
    │   │       └─ application.yml ...................... configuration file
    │   │
    │   └── test
    │       ├── java
    │       ├── kotlin
    │       │   └─ com.gnirps.project-package
    │       │      ├── com.gnirps.keycloak.config ........................... configuration
    │       │      ├── controller ....................... endpoints
    │       │      ├── mapper ........................... dto to/from model converter 
    │       │      └── service .......................... core logic's operations definition
    │       └── resources
    │
    ├── Dockerfile ...................................... dependencies, building and deployment management
    │
    ├── pom.xml ......................................... dependencies, building and deployment management
    │
    └── README.md ....................................... if you're reading this, you should know what it is...

## Nexus Repository

A Nexus Repository shall be used to store and retrieve internal dependencies, as well as persist locally external 
dependencies to ensure their availability for the years to come.

### Deployment

The following code can be used inside a docker-compose file to generate a default image of a Nexus repository. 
The default admin user is called ***admin*** and his password can be found in the following location: 
`[/var/lib/docker/volumes/{volume-name}/]_data/admin.password`.
```
volumes:
  nexus-volume:

service:
  nexus:
    image: sonatype/nexus3
    ports:
      - 9000:8081
    volumes:
      - "nexus-volume:/nexus-data"
```

One can then reach for the port ***9000*** on the hosting machine to configure the repository by creating:

- Two **_maven2 (hosted)_** repositories, called **_gnirps-snapshots_** and **_gnirps-releases_**, 
for example. Allowing redeployment can be done (or not, depending on local policies). They will be used to host internal
dependencies.
- One **_maven2 (proxy)_** repository, called **_gnirps-maven_**, for example, and with a mixed Layout Policy 
allowing both releases and snapshots versions proxying the following url: *http://repo1.maven.org/maven2/*
- One **_maven2 (group)_** to regroup the previous three; let's call it **_gnirps_** for the sake of example.

It can thereafter be used by following two simple steps:

### Nexus usage

First, one has to update its **~/.m2/settings.xml** file (or create it if it doesn't exist) with the following setup:

```<?xml version="1.0" encoding="UTF-8"?>
   <settings xmlns="http://maven.apache.org/SETTINGS/1.1.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
     
      <servers>
        <server>
          <id>gnirps-snapshots</id>
          <username>admin</username>
          <password>{admin-password}</password>
        </server>
        <server>
          <id>gnirps-releases</id>
          <username>admin</username>
          <password>{admin-password}</password>
        </server>
      </servers>
    
      <mirrors>
        <mirror>
          <id>gnirps</id>
          <name>gnirps</name>
          <url>{host-address}:9000/repository/gnirps/</url>
          <mirrorOf>*</mirrorOf>
        </mirror>
      </mirrors>
      <!-- -->
    
    </settings>
```

Then, one can simply include the following piece of maven code in whichever's project **pom.xml** file (replacing 
*host* with the host address, obviously):
```
    <!-- to download from our nexus (which forwards to maven central if need be) -->
    <repositories>
        <repository>
            <id>gnirps</id>
            <url>{host-address}:9000/repository/gnirps/</url>
        </repository>
    </repositories>

    <!-- to upload towards our nexus -->
    <distributionManagement>
        <snapshotRepository>
            <id>gnirps-snapshots</id>
            <url>{host-address}:9000/repository/gnirps-snapshots/</url>
        </snapshotRepository>
        <repository>
            <id>gnirps-releases</id>
            <url>{host-address}:9000/repository/gnirps-releases/</url>
        </repository>
    </distributionManagement>
```

Those will be inherited from **gnirps-origin** if the project references it as its parent in ***maven***.

## License

REDLab MIT (maybe?)
