package org.chewing.v1.implementation.auth

import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class AuthProcessor(
    private val userAppender: UserAppender,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authAppender: AuthAppender,
    private val authRemover: AuthRemover,
) {
    fun processLogin(
        contact: Contact
    ): Pair<JwtToken, User> {
        val user = userAppender.appendIfNotExist(contact)
        val token = jwtTokenProvider.createJwtToken(user.userId)
        authAppender.appendLoggedIn(token.refreshToken, user.userId)
        return Pair(token, user)
    }

    fun processLogout(
        refreshToken: String
    ) {
        jwtTokenProvider.validateToken(refreshToken)
        authRemover.removeLoginInfo(refreshToken)
    }

    fun processRefreshToken(
        refreshToken: String
    ): JwtToken {
        val token = jwtTokenProvider.cleanedToken(refreshToken)
        // 리프레시 토큰 유효성 검사(수정)
        jwtTokenProvider.validateRefreshToken(token)
        // 리프레시 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        return jwtTokenProvider.createJwtToken(userId)
    }
}