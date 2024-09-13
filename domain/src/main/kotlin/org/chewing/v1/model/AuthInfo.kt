package org.chewing.v1.model

import org.chewing.v1.model.token.RefreshToken

class AuthInfo(
    val authInfoId: String,
    val phone: Phone,
    val email: Email,
    // val refreshToken: RefreshToken? // 여기에 refreshToken을 필드로 추가
) {
    companion object {
        fun of(
            authInfoId: String,
            phone: Phone,
            email: Email,
            refreshToken: RefreshToken? = null // 기본값을 null로 설정
        ): AuthInfo {
            return AuthInfo(
                authInfoId = authInfoId,
                phone = phone,
                email = email,
                // refreshToken = refreshToken // refreshToken을 여기서 설정
            )
        }

        fun onlyWithId(
            authInfoId: String
        ): AuthInfo {
            return AuthInfo(
                authInfoId = authInfoId,
                phone = Phone.generate("", ""),
                email = Email.generate(""),
                // refreshToken = null // 처음에는 null로 설정
            )
        }
    }
}