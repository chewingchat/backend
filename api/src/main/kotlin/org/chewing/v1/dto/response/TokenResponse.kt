package org.chewing.v1.dto.response

import org.chewing.v1.model.User

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(accessToken: String, refreshToken: String): TokenResponse {
            return TokenResponse(accessToken, refreshToken)
        }
    }
}