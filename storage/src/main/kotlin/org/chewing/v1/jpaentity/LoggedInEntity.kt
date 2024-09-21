package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.token.RefreshToken
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "logged_in", schema = "chewing")
class LoggedInEntity(
    @Id
    @Column(name = "logged_in_id")
    val loggedInId: String = UUID.randomUUID().toString(),

    @Column(name = "refresh_token")
    val refreshToken: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auth_id", nullable = false)
    val auth: AuthJpaEntity,

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime
) {
    companion object {
        fun fromAuthInfo(authInfo: AuthInfo, refreshToken: RefreshToken): LoggedInEntity {
            return LoggedInEntity(
                refreshToken = refreshToken.token,
                auth = AuthJpaEntity.fromAuthInfo(authInfo),
                expiredAt = refreshToken.expiredAt
            )
        }


    }
    // 사용자 ID를 반환하는 메서드 추가
    fun getUserId(): String {
        return auth.user.id  // auth.user를 통해 userId 반환
    }
}