package com.gnirps.logging.exceptions

class BashException(val exitCode: Int, message: String) : Exception(message)
