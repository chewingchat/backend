package org.chewing.v1.dto.response.auth

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