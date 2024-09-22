package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.auth.AuthInfo
import org.chewing.v1.model.User
import java.util.*

@Entity
@Table(name = "auth", schema = "chewing")
internal class AuthJpaEntity(
    @Id
    @Column(name = "auth_id")
    val authId: String = UUID.randomUUID().toString(), // 수정함

    val userId: String,

    val emailId: String?,

    val phoneNumberId: String?
) {
    companion object {
        fun generateEmail(emailId: String, userId: String): AuthJpaEntity {
            return AuthJpaEntity(
                userId = userId,
                emailId = emailId,
                phoneNumberId = null
            )
        }
        fun generatePhoneNumber(phoneNumberId: String, userId: String): AuthJpaEntity {
            return AuthJpaEntity(
                userId = userId,
                emailId = null,
                phoneNumberId = phoneNumberId
            )
        }
    }

    fun toAuthInfo(): AuthInfo {
        return AuthInfo.of(authId, userId)
    }
}