package org.chewing.v1.repository

import org.chewing.v1.repository.support.JpaDataGenerator
import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.auth.LoggedInJpaRepository
import org.chewing.v1.repository.auth.LoggedInRepositoryImpl
import org.chewing.v1.repository.support.RefreshTokenProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LoggedInRepositoryImplTest : JpaContextTest() {
    @Autowired
    private lateinit var loggedInJpaRepository: LoggedInJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val loggedInRepositoryImpl: LoggedInRepositoryImpl by lazy {
        LoggedInRepositoryImpl(loggedInJpaRepository)
    }

    @Test
    fun `로그인 정보를 삭제해야 한다`() {
        val refreshToken = RefreshTokenProvider.buildNormal()
        jpaDataGenerator.loggedInEntityData(refreshToken)
        loggedInRepositoryImpl.remove(refreshToken.token)

        val result = loggedInJpaRepository.findByRefreshToken(refreshToken.token)
        assert(result.isEmpty)

    }

    @Test
    fun `로그인 정보를 수정 해야한다`() {
        val refreshToken = RefreshTokenProvider.buildNormal()
        jpaDataGenerator.loggedInEntityData(refreshToken)

        val newRefreshToken = RefreshTokenProvider.buildNew()

        loggedInRepositoryImpl.update(newRefreshToken, refreshToken)

        val result = loggedInJpaRepository.findByRefreshToken(newRefreshToken.token)

        assert(result.isPresent)
        assert(result.get().toRefreshToken().token == newRefreshToken.token)
    }

    @Test
    fun `로그인 정보를 추가해야 한다`() {
        val refreshToken = RefreshTokenProvider.buildNormal()
        loggedInRepositoryImpl.append(refreshToken, "userId")

        val result = loggedInJpaRepository.findByRefreshToken(refreshToken.token)
        assert(result.isPresent)
        assert(result.get().toRefreshToken().token == refreshToken.token)
    }

    @Test
    fun `리프레시 토큰을 조회해야 한다`() {
        val refreshToken = RefreshTokenProvider.buildNormal()
        jpaDataGenerator.loggedInEntityData(refreshToken)


        val result = loggedInRepositoryImpl.read(refreshToken.token, "userId")
        assert(result != null)
        assert(result!!.token == refreshToken.token)
    }
}