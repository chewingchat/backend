package org.chewing.v1.implementation.auth

import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component

@Component
class AuthProcessor(
    private val userAppender: UserAppender,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authAppender: AuthAppender
) {
    fun processLogin(
        contact: Contact
    ): Pair<JwtToken, User>{
        val user = userAppender.appendIfNotExist(contact)
        val token = jwtTokenProvider.createJwtToken(user.userId)
        authAppender.appendLoggedIn(token.refreshToken, user)
        return Pair(token, user)
    }
}