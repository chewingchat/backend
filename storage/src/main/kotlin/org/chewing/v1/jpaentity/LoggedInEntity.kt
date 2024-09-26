package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.user.User
import org.chewing.v1.model.token.RefreshToken
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "logged_in", schema = "chewing")
internal class LoggedInEntity(
    @Id
    @Column(name = "logged_in_id")
    val loggedInId: String = UUID.randomUUID().toString(),

    @Column(name = "refresh_token")
    val refreshToken: String,

    val userId: String,

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime
) {
    companion object {
        fun fromToken(refreshToken: RefreshToken, user: User): LoggedInEntity {
            return LoggedInEntity(
                refreshToken = refreshToken.token,
                userId = user.userId,
                expiredAt = refreshToken.expiredAt
            )
        }
    }
}