package org.chewing.v1.repository

import org.chewing.v1.jpaentity.auth.LoggedInJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
internal class LoggedInRepositoryImpl(
    private val loggedInJpaRepository: LoggedInJpaRepository,
) : LoggedInRepository {
    override fun remove(refreshToken: String) {
        loggedInJpaRepository.deleteByRefreshToken(refreshToken)
    }

    override fun append(refreshToken: RefreshToken, userId: String) {
        loggedInJpaRepository.save(LoggedInJpaEntity.generate(refreshToken, userId))
    }

    override fun update(refreshToken: RefreshToken, preRefreshToken: RefreshToken) {
        loggedInJpaRepository.findByRefreshToken(preRefreshToken.token).ifPresent {
            it.updateRefreshToken(refreshToken)
            loggedInJpaRepository.save(it)
        }
    }

    override fun read(refreshToken: String, userId: String): RefreshToken? {
        return loggedInJpaRepository.findByRefreshTokenAndUserId(refreshToken, userId).orElse(null).toRefreshToken()
    }
}
