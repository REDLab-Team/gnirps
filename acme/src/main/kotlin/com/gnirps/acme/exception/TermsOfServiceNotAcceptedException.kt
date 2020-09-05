package com.gnirps.acme.exception

class TermsOfServiceNotAcceptedException(
        msg: String = "Terms of service must be accepted in properties file."
) : Exception(msg)