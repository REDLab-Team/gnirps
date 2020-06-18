package com.gnirps.jwt.annotations

import com.gnirps.jwt.config.SecurityConstants
import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("hasRole('${SecurityConstants.ADMIN}')")
annotation class AdminAccess