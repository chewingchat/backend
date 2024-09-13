
package org.chewing.v1.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val principal: String,
    private val credentials: Any?,
    authorities: Collection<GrantedAuthority>?
) : AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): Any? {
        return credentials
    }

    override fun getPrincipal(): Any? {
        return principal
    }

    init {
        isAuthenticated = true
    }
}