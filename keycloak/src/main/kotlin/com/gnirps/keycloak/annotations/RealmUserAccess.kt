package com.gnirps.keycloak.annotations

import com.gnirps.keycloak.config.SecurityConstants
import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("hasRole('${SecurityConstants.REALM_USER}')")
annotation class RealmUserAccess