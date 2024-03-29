                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                  
                                          _________  ____ ___  ____ ___  ____  ____  _____
                                         / ___/ __ \/ __ `__ \/ __ `__ \/ __ \/ __ \/ ___/
                                        / /__/ /_/ / / / / / / / / / / / /_/ / / / (__  ) 
                                        \___/\____/_/ /_/ /_/_/ /_/ /_/\____/_/ /_/____/  
               
## Summary

This ***GNIRPS*** module regroups some of the most-used and must-use features of the framework, to avoid code 
redundancy or heavy disparities between children projects. The most noticing ones are the following.

## Contents

- [Properties sourcing](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#properties-sourcing)
- [Loggers injection](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#loggers-injection)
- [Centralised definitions](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#centralised-definitions):
    - [Errors and exceptions](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#errors-and-exceptions)
    - [Models and dtos](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#models-and-dtos)
    - [PageRequest](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#pagerequest)
    - [ExecutionTimer](https://github.com/REDLab-Team/gnirps/tree/master/src/commons#executiontimer)
    
## Properties sourcing

The automated properties sourcing allows an out-of-the-box configuration for any new project, and relies on two things:
- Spring's property sourcing mechanism and its priority system, which will allow the loading of each module's defaults 
settings before overwriting them with the user's ones (found in 
`com.gnirps.commons.com.gnirps.keycloak.config.properties.CustomPropertySources`)
- The use of several `{module-name}-application.yml` and one optional `application.yml` file, properly parsed thanks to 
a custom `com.gnirps.commons.com.gnirps.keycloak.config.properties.YamlPropertySourceFactory`, meant to evolve quite a bit in the 
future to remove some leftover boilerplate from Spring in Java which won't stay in Kotlin.

As per convention, any module which will load properties from such files will use a 
`basePackage.com.gnirps.keycloak.config.{module-name}Properties` class to do so. This could most likely be someday centralised in a single 
properties loader which would parse all files - but it isn't yet.

## Loggers injection

To avoid disparities between modules (or eventually projects) when logging events, a Logger interface has been defined 
in this module, as well as a default Spring Bean using an slf4jLogger to implement said interface.

Defining an immutable property of the `com.gnirps.commons.logging.service.Logger` type in any class will directly provide 
a suitable logger. Hence, no matter what evolution or implementation is chosen in the future, one simply has to comply 
with the interface and redefine a primary bean to inject his own logger in his project (or eventually the whole 
framework).

Assuming that someday, someone would like to automate the sending of logs to an API or database for analytics reasons, 
he would only have to make use of those instructions to make it work anywhere.

```
import com.gnirps.commons.logging.service.Logger
class Example(private val logger: Logger) {
    fun doSomething() {
        ...
        logger.info("i've done it")
        ...
        logger.log(
            type = Logger.Type.OPERATION,
            level = Logger.Level.INFO,
            message = "i mean i really do"
        )
    } 
}
```

## Centralised definitions

#### Errors and exceptions
Though it isn't the case yet, several modules will likely someday rely on the same custom exceptions and errors: for 
example, if we someday implement a custom authorisation system, relevant errors will need to be shared between 
the module Swagger (for its interceptors), the authorisation module, and any API using it.


#### Models and dtos
Hence, this module is meant to regroup those definitions in one place. In the same way, some models or DTOs could also 
be shared at some point and find their place here.

#### PageRequest
As of now, the only tool (apart from the ones mentionned in two first sections of this document) that really is shared 
between several projects is the PageRequest generator `com.gnirps.utils.CustomPageRequest`. Its job is to allow 
the use of the Pageable objects easily in any controller or repository, for example.

#### ExecutionTimer

Inline function allowing a most convenient feature to measure program's efficiency by offering the possibility to time 
the execution of a block of code as follows:
```
val executionTime: Long = measureTimeMillis {
    ...
}
```

## Error handling
The error handling aimed at emailing the raised errors to follow them.

### Principles
If an error occurred, the mailing configuration is checked and the mail is sent only with the folowing configuration : 
* the send mail feature is enabled: set param 'gnirps.core.mail-sender.enable=true' into 'application.yml'
* hosts, credentials and recipients are valid

### Mail notifications
The Google SMTP is no longer available.
We have to configure the mailing information. We have two ways to do this :
- define the following environment variables :
  - 'GNIRPS.CORE.MAIL.SENDER.HOST'
  - 'GNIRPS.CORE.MAIL.SENDER.USERNAME'
  - 'GNIRPS.CORE.MAIL.SENDER.PASSWORD'
  - 'GNIRPS.CORE.MAIL.SENDER.RECIPIENTS'
- or overwrite the following properties in 'application.yml'
  - 'gnirps.core.mail.sender.host'
  - 'gnirps.core.mail.sender.username'
  - 'gnirps.core.mail.sender.password'
  - 'gnirps.core.mail.sender.recipients'
