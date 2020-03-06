package com.gnirps.commons.exceptions

class BashException(val exitCode: Int, message: String): Exception(message)
