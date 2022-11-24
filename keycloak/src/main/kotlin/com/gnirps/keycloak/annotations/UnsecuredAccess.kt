package com.gnirps.keycloak.annotations

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("permitAll()")
@RequiresOptIn
// Not functional yet:
//      Keycloak is blocking unauthenticated access to any endpoint that has not
//      been registered as open; hence, Spring Security annotations being only
//      taken into account if Keycloak has let a request through, this one will
//      not work unless it somehow tells Keycloak to let go.
annotation class UnsecuredAccess
