package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(name = "auth", schema = "chewing")
class AuthJpaEntity(
    @Id
    @Column(name = "auth_id")
    val authId: String = UUID.randomUUID().toString(), // 수정함

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "email_id", nullable = true)
    val email: EmailJpaEntity?,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "phone_number_id", nullable = true)
    val phoneNumber: PhoneNumberJpaEntity?
) {
    companion object {
        fun fromAuthInfo(authInfo: AuthInfo): AuthJpaEntity {
            return AuthJpaEntity(
                authId = authInfo.authInfoId,
                user = UserJpaEntity.fromUser(User.empty()),
                email = EmailJpaEntity.fromEmail(authInfo.email),
                phoneNumber = PhoneNumberJpaEntity.fromPhone(authInfo.phone)
            )
        }
    }

    fun toUser(): User {
        return user.toUser()
    }

    fun toAuthInfoOnlyWithId(): AuthInfo {
        return AuthInfo.onlyWithId(authId)
    }
}