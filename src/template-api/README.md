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

This template is meant to be copied as a starting point for any API project. By its own, it provides an out-of-the-box 
setup of SpringBoot, Maven, Swagger and Docker. One should simply provide modifications to three files before being on 
his own:
- the `src/main/resources/application.yml` file.
- the `pom.xml` file: update the **name** and **artifact-id**, and add any required dependency.
- change the *template* in `src/main/kotlin/com/gnirps/template` directory name by the project's name.