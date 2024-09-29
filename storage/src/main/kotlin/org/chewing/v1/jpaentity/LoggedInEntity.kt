package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.auth.LoginInfo
import org.chewing.v1.model.user.User
import org.chewing.v1.model.token.RefreshToken
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "logged_in", schema = "chewing")
internal class LoggedInEntity(
    @Id
    @Column(name = "logged_in_id")
    private val loggedInId: String = UUID.randomUUID().toString(),

    @Column(name = "refresh_token")
    private var refreshToken: String,

    private val userId: String,

    @Column(name = "expired_at")
    private var expiredAt: LocalDateTime
) {
    companion object {
        fun fromToken(refreshToken: RefreshToken, userId: String): LoggedInEntity {
            return LoggedInEntity(
                refreshToken = refreshToken.token,
                userId = userId,
                expiredAt = refreshToken.expiredAt
            )
        }
    }

    fun toLoggedInId(): String {
        return loggedInId
    }

    fun updateRefreshToken(refreshToken: RefreshToken) {
        this.refreshToken = refreshToken.token
        this.expiredAt = refreshToken.expiredAt
    }
}