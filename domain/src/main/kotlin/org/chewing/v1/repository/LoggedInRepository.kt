package org.chewing.v1.repository


import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

interface LoggedInRepository {
    fun removeLoginInfo(userId: String)
    fun appendLoggedIn(refreshToken: RefreshToken, loggedInId: String)
    fun readLoggedId(refreshToken: String): String?
}