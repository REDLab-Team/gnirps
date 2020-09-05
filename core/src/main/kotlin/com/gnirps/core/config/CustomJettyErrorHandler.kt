package com.gnirps.core.config

import org.eclipse.jetty.servlet.ErrorPageErrorHandler
import org.springframework.stereotype.Component

@Component
class CustomJettyErrorHandler : ErrorPageErrorHandler()