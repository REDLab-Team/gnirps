package com.gnirps.acme.exception

import java.lang.Exception

class TermsOfServiceNotAcceptedException(
    msg: String = "Terms of service must be accepted in properties file."
): Exception(msg)