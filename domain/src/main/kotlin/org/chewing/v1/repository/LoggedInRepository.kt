package org.chewing.v1.repository


import org.chewing.v1.model.token.RefreshToken

interface LoggedInRepository {
    fun remove(refreshToken: String)
    fun append(refreshToken: RefreshToken, userId: String)

    fun update(refreshToken: RefreshToken, preRefreshToken: RefreshToken)
    fun read(refreshToken: String): RefreshToken?
}