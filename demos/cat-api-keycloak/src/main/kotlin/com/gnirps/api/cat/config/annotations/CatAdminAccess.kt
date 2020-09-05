package com.gnirps.api.cat.config.annotations

import com.gnirps.api.cat.config.SecurityConstants
import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("hasRole('${SecurityConstants.CAT_ADMIN}')")
annotation class CatAdminAccess