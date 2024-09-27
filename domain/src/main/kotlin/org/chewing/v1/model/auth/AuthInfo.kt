package org.chewing.v1.model.auth

import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone

class AuthInfo(
    val authInfoId: String,
    val userId: String
    // val refreshToken: RefreshToken? // 여기에 refreshToken을 필드로 추가
) {
    companion object {
        fun of(
            authInfoId: String,
            userId: String
        ): AuthInfo {
            return AuthInfo(
                authInfoId = authInfoId,
                userId = userId
            )
        }
    }
}