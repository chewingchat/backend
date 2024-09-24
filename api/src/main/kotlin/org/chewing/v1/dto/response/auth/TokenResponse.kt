package org.chewing.v1.dto.response.auth

import org.chewing.v1.model.auth.JwtToken

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(token: JwtToken): TokenResponse {
            return TokenResponse(token.accessToken, token.refreshToken.token)
        }
    }
}