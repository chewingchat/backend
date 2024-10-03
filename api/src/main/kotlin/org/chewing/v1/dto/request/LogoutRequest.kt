package org.chewing.v1.dto.request

data class LogoutRequest(
    val refreshToken: String = ""
) {
    fun toRefreshToken(): String {
        return refreshToken
    }
}