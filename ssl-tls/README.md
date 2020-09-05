                 ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                 ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                 
                 
                       __________ __       ________   _____
                      / ___/ ___// /      /_  __/ /  / ___/
                      \__ \\__ \/ /  ______/ / / /   \__ \ 
                     ___/ /__/ / /__/_____/ / / /______/ / 
                    /____/____/_____/    /_/ /_____/____/  
                                       
                                         
                                         
## Summary

This module allows the generation of SSL / TLS certificate.
It also allows the management of self-signed certificate.

## Content

- Implementation
- Properties
- Script

## Implementation

It is based on the use of Spring Security dependency.

## Properties

The following properties are imported by default:

```
server:
  ssl:
    key-store-type: ${SSL_KEYSTORE_TYPE}
    key-store:
      classpath: ${SSL_KEYSTORE_CLASSPATH}
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-alias: ${SSL_KEY_ALIAS}

  security:
    require-ssl: true

  trust:
    store:
      classpath: ${SSL_TRUSTSTORE_CLASSPATH}
      password: ${SSL_TRUSTSTORE_PASSWORD}
```

As you may have noticed, some rely on environment variables, which are most often provided by a stack or docker-compose 
file in a Docker environment.

More comprehensively: 
- ``ssl.key-store-type``: defines the format used for the keystore
- ``ssl.key-store.classpath``: defines the path to the keystore containing the certificate
- ``ssl.key-store-password``: defines the password used to generate the certificate
- ``ssl.key-alias``: defines the alias mapped to the certificate

- ``security.require-ssl``: allows to accept only https requests

- ``trust.store.classpath``: defines the trust store location
- ``trust.store.password``: defines the trust store password

## Script