package org.chewing.v1.repository

import org.chewing.v1.jparepository.*
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
internal class AuthRepositoryImpl(
    private val loggedInJpaRepository: LoggedInJpaRepository,
) : LoggedInRepository {
    override fun removeLoginInfo(userId: String) {
        loggedInJpaRepository.deleteByUserId(userId)
    }
    override fun appendLoggedIn(refreshToken: RefreshToken, loggedInId: String) {
        loggedInJpaRepository.findById(loggedInId).ifPresent {
            it.updateRefreshToken(refreshToken)
            loggedInJpaRepository.save(it)
        }
    }
    override fun readLoggedId(refreshToken: String): String? {
        return loggedInJpaRepository.findByRefreshToken(refreshToken).orElse(null).toLoggedInId()
    }
}
