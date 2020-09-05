                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                  
                                           ______      ______ _____ _____ ____  _____
                                          / ___/ | /| / / __ `/ __ `/ __ `/ _ \/ ___/
                                         (__  )| |/ |/ / /_/ / /_/ / /_/ /  __/ /    
                                        /____/ |__/|__/\__,_/\__, /\__, /\___/_/     
                                                            /____//____/             
## Summary

This module is the actual base of the whole framework - *it all started here*. The funding idea was to:
 - ease as much as possible the use of Spring 
 - while providing an out-of-the-box solution, assembling several tools such as a Swagger documentation and a Docker 
 setup
 - and suggest a layered architecture to give a proper structure to APIs.

Other tools and functionality came in later, as well as a finer tuning of the original ones.

## Contents

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

## SpringBoot

Spring offer powerful tools to set up APIs, and SpringBoot offers basic settings to get an API running even faster. 
However, one would still have to dig deep in Spring code to fine-tune some things or replace others. Eventually, even 
with SpringBoot, one would still have quite some work to do to get a working setup with a database connector and an 
error interceptor or other such features.

The `com.gnirps.swagger.SpringBootEntryPoint` is the main class which powers up SpringBoot. It defines the first 
packages that Spring will scan to load up its beans. Furthermore, it offers a restart feature which will come in handy 
for some modules such as Acme which might require a reset of Spring's context after some direct files modifications.

## Docker secrets

Docker secrets can be used easily by creating them on the host machine and referencing them as if they were environment 
variables but with names matching `"{DOCKER-SECRET:secret-name}"`. They will then be loaded as proper environment 
variables within the container, at startup, thanks to the script `src/main/resources/load_docker-secrets_as_env-var.sh`.

## Swagger

An user-end documentation can be generated from code annotations thanks to Swagger. The base settings are defined in 
the `swagger-application.yml` file and overwritten by any eventual `application.yml` file in the inheriting projects.

```
gnirps:
  common:
    base-package: "com.gnirps"
    service-name: "missing: gnirps.common.service-name"
  swagger:
    base-package: ${gnirps.common.base-package}
    api:
      title: "missing-property:gnirps.swagger.title"
      description: "missing-property:gnirps.swagger.description"
      version: "missing-property:gnirps.common.version"
      license: "License: MIT"
      license-url:
      terms-of-service:
    maintainer:
      name: "Nicolas Pittion-Rossillon"
      email: "nicolas.pittion.ext@gnirps.com"
      website:

logging:
  level:
    root: INFO
    org.springframework.web: INFO
    com.gnirps: DEBUG # dis not wurkin yet
```

From there on, the controller method's (defining endpoints) can be annotated to figure in the auto-generated 
documentation, for example as follows:

```
    @GetMapping("request-suffix")
    @ApiOperation(value = "description")
    @ApiResponses(ApiResponse(code = 200, message = "message"))
    @ResponseStatus(HttpStatus.OK)
    fun findAll(
            @ApiParam(
                    value = "Offset for the pagination of the results.",
                    example = "0"
            ) @RequestParam(required = false) page: Int?,
            @ApiParam(
                    value = "Maximum number of results expected.",
                    example = "10"
            ) @RequestParam(required = false) size: Int?,
            @ApiParam(
                    value = "Ordering direction of the results.",
                    example = "asc"
            ) @RequestParam(required = false) direction: String?,
            @ApiParam(
                    value = "Sorting properties per order of priority.",
                    example = "name, date"
            ) @RequestParam(required = false) properties: Array<String>?
    ): Page<EntityResponse> {
        return service
                .findAll(
                    CustomPageRequest(
                            page =page,
                            size =size,
                            direction = direction,
                            properties = properties?: arrayOf("prop1", "prop3")
                    ).toPageRequest()
                ).map{Mapper.toResponse(it)}
    }
```

## Cross Origin Request Sharing
 
It was an implementation choice to allow requests from any origin by default. Should one decide otherwise, he could 
make those settings importable from the configuration file or offer alternative beans. The current settings are defined 
by the class `com.gnirps.swagger.com.gnirps.keycloak.config.CorsFilterConfiguration`.

## Errors and Exceptions Interceptor

Three classes are used to set up an interceptor for any error that may happen within the API:
- `com.gnirps.swagger.controller.ExceptionHandlerController`: allows a global handling of any exception or error in the 
application.
- `com.gnirps.swagger.controller.RestTemplateResponseErrorHandler`: allows a global handling for a specific subset of 
errors - those thrown by an HTTP request within the API.
- `com.gnirps.swagger.com.gnirps.keycloak.config.RestTemplateConfiguration`: allows the previous one to be used.

The exception handling is most easily extendable (at some point, it looked like this):
```
@ExceptionHandler(Exception::class)
fun handleAll(exception: Exception): ResponseEntity<Any> {
    return when (exception) {
        is JpaObjectRetrievalFailureException,
        is EntityNotFoundException          -> logAndFormat(exception, HttpStatus.NOT_FOUND)
        is EntityExistsException            -> logAndFormat(exception, HttpStatus.SEE_OTHER)
        is ConstraintViolationException,
        is MissingServletRequestParameterException,
        is MethodArgumentNotValidException  -> logAndFormat(exception, HttpStatus.BAD_REQUEST)
        is ResourceAccessException          -> logAndFormat(exception, HttpStatus.BAD_GATEWAY)
        is AccessDeniedException            -> logAndFormat(exception, HttpStatus.UNAUTHORIZED)
        is HttpException                    -> logAndFormat(exception, exception.status)
        is BashException                    -> logAndFormat(exception, HttpStatus.INTERNAL_SERVER_ERROR)
        else                                -> logAndFormat(exception, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
```

## Logger Interceptor

Very much in the way of Aspect-Oriented Programming, a mechanism meant to allow the logging of http requests was 
implemented. It can trigger methods at the reception of a request, or after its completion. By default, the latter was 
chosen to keep a trace of both the request and its result on the standard output.

The class implementing this feature is `com.gnirps.swagger.controller.LoggerInterceptor`.

## Root redirection to documentation

A debatable implementation choice was to define a redirection from the root of the API towards its Swagger 
documentation, or more precisely its web UI : `com.gnirps.swagger.controller.RootController`.