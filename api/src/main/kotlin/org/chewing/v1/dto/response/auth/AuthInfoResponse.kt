package org.chewing.v1.dto.response.auth

import org.chewing.v1.model.user.User
import org.chewing.v1.model.auth.JwtToken

data class AuthInfoResponse(
    val token: TokenResponse,
    val authStatus: String
) {
    companion object {
        fun of(token: JwtToken, user: User): AuthInfoResponse {
            val tokenResponse = TokenResponse.of(token)
            return AuthInfoResponse(tokenResponse, user.type.toString().lowercase())
        }
    }
}