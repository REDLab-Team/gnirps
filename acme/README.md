                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                  ____ __________ ___  ___ 
                                                 / __ `/ ___/ __ `__ \/ _ \
                                                / /_/ / /__/ / / / / /  __/
                                                \__,_/\___/_/ /_/ /_/\___/ 
                                                                           
## Summary

This module offers a free way to provide **HTTPS** support to an API, thanks to **Let's Encrypt**. It works 
out-of-the-box under the condition that the API is served under a domain-name.

## Contents

- [Properties](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#properties)
- [SSL certificates generated at startup](
https://github.com/REDLab-Team/gnirps/tree/master/src/acme#ssl-certificates-generated-at-startup)
- [Http Challenge](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#http-challenge)
- [LE Terms of service](https://github.com/REDLab-Team/gnirps/tree/master/src/acme#le-terms-of-service)

## Properties

As usual, the properties are located in a `src/main/resources/application.yml` file:
```
gnirps:
  common:
    service-name: "missing:gnirps.common.service-name"
  acme:
    terms-of-service-agreed: false
    endpoint: staging
    organisation: gnirps
    contact-mail: nicolas.pittion.ext@gnirps.com
    account-login-url:
    keys:
      user: user-key.pem
      domain: user-domain.pem
    domain:
      name: "missing:gnirps.acme.domain.name"
      csr-path: domain.csr
      crt-path: domain.crt
    keystore:
      file-path: keystore.jks
      password: keystore-password
```

## SSL certificates generated at startup

The SSL certificate required to secure an API through HTTPS is generated by `com.gnirps.acme.service.AcmeService` during 
runtime, at the startup of the service. 

However, considering that it generates new files (a certificate request at 
first, and a certificate itself when the request is validated) on the hard-drive which have to be loaded in Spring's 
context when the BeansPostProcessors are called, the application needs to be restarted once a certificate has been 
created. This is done by `com.gnirps.acme.config.ApplicationStart` **only when needed** - which is to say when no 
certificate exist, or when the existing certificate is obsolete.

## Http Challenge

The way to validate a certificate request is to **prove ownership** of a server linked to its domain name: this is done 
by creating a file with a specific content and serving it on an endpoint exposed through http requests.

Here, the chosen validating entity is **Let's Encrypt**, for it doesn't charge money for that service. It offers its 
service under certain limitations, which is why using the **staging endpoint** when testing is recommended.

Various services can be found in the relevant directory to handle the order creation, challenge file's storage in 
memory, etc.

## LE Terms of service

Since the use of Let's Encrypt requires agreeing to its terms of use, the user will have to certify having done so by 
updating a field in the `application.yml` file before using this module. Should he forget that part, a custom 
`com.gnirps.acme.exception.TermsOfServiceNotAcceptedException` will be thrown at him.