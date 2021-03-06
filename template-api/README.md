                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                              __                       __      __     
                                             / /____  ____ ___  ____  / /___ _/ /____ 
                                            / __/ _ \/ __ `__ \/ __ \/ / __ `/ __/ _ \
                                           / /_/  __/ / / / / / /_/ / / /_/ / /_/  __/
                                           \__/\___/_/ /_/ /_/ .___/_/\__,_/\__/\___/ 
                                                            /_/                        
## Summary

This template is meant to be copied as a starting point for any API project. On its own, it provides an out-of-the-box 
setup of SpringBoot, Maven, Swagger and Docker. 

## Contents

- [Application settings](#application-settings)
- [Docker settings](#docker-settings)

## Application settings

One should simply provide modifications to three files to setup his API:
- the `src/main/resources/application.yml` file.
- the `pom.xml` file: update the **name** and **artifact-id**, and add any required dependency.
- change the *template* in `src/main/kotlin/com/gnirps/template` directory name by the project's name.


## Docker settings

One should (if need be):
- update the `Dockerfile` at the root of the project to customize his docker image.
- update the **docker.image**[***.prefix***|***.name***|***.tag***].
- fix up a `stack.yml` file at the root to manage the deployment part.

In the following stack example, a **template-api** service will be started with three docker secrets expanded as 
environment variable (within the container, during the build phase).

```
version: '3.7'

secrets:
    my-user:
        external: true
    my-password:
        external: true
    my-url:
        external: true

services:
    template-api:
        image: pittinic/gnirps:template
        ports:
            - '9001:8080'
        secrets:
            - my-user
            - my-password
            - my-url
        environment:
            MY_USER: "{DOCKER-SECRET:my-user}"
            MY_PASSWORD: "{DOCKER-SECRET:my-password}"
            MY_URL: "{DOCKER-SECRET:my-url}"
            ENV_SECRETS_DEBUG: "true"
```

Said secrets can be generated by calling the following bash code: ``bash bin/create_secrets user password url``.