                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                  __                    _       _          __ 
                                      _________ _/ /_      ____ _____  (_)     (_)      __/ /_
                                     / ___/ __ `/ __/_____/ __ `/ __ \/ /_____/ / | /| / / __/
                                    / /__/ /_/ / /_/_____/ /_/ / /_/ / /_____/ /| |/ |/ / /_  
                                    \___/\__,_/\__/      \__,_/ .___/_/   __/ / |__/|__/\__/  
                                                             /_/         /___/                    
       
## Summary

This API is a simple example of the layered-architecture we recommend applied to a couple modules of the framework. 
Those are:
- `swagger`: a module containing *SpringBoot*, *Swagger* and *Docker*.
- `postgresql-database`: a module containing *Jpa*, *Hibernate* and *PostgreSQL*.
- `jwt`: a module containing *Spring Security* and *Jjwt*

## Layered architecture

    ├── src
    │   │
    │   ├── main
    │   │   ├── java
    │   │   ├── kotlin
    │   │   │   └─ com.gnirps.api.cat
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
    │       │   └─ com.gnirps.api.cat
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
    └── README.md ....................................... the document you're reading right now

## How to use

The easiest way is to deploy both the database and api using docker-compose (check the `docker-compose.yml` file) with 
the following command: `docker-compose up`.

Should one prefer to deploy a stack or bare containers, he's free to - as long as he takes care to map the ports and 
set up the secrets and **environment variables** (and/or **docker secrets**) properly.