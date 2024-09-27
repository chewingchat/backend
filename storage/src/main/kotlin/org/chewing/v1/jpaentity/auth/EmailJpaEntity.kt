package org.chewing.v1.jpaentity.auth

import jakarta.persistence.*;
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.contact.Email
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime
import java.util.*

@Entity
@DynamicInsert
@Table(name = "email", schema = "chewing")
internal class EmailJpaEntity(
    @Id
    @Column(name = "email_id")
    private val emailId: String = UUID.randomUUID().toString(),

    @Column(name = "email")
    private var emailAddress: String, // email 수정

    @Column(name = "first_authorized")
    private var firstAuthorized: Boolean,

    @Column(name = "expired_at")
    private var expiredAt: LocalDateTime = LocalDateTime.now().plusMinutes(5),

    @Column(name = "authorized_number")
    private var authorizedNumber: String = UUID.randomUUID().toString().replace("-", "").take(4),
) : BaseEntity() {
    companion object {
        fun fromEmail(email: Email): EmailJpaEntity {
            return EmailJpaEntity(
                emailId = email.emailId,
                emailAddress = email.emailAddress,
                firstAuthorized = email.isAuthorizedFirst,
            )
        }

        fun generate(email: EmailAddress): EmailJpaEntity {
            return EmailJpaEntity(
                emailAddress = email.email,
                firstAuthorized = false,
            )
        }
    }
    fun toEmail(): Email {
        return Email.of(
            emailId = emailId,
            emailAddress = emailAddress,
            authorizedNumber = authorizedNumber,
            expiredTime = expiredAt,
            isAuthorized = firstAuthorized,
        )
    }

    fun updateFirstAuthorized() {
        firstAuthorized = true
    }

    fun getAuthorizedNumber(): String {
        return authorizedNumber
    }

    fun updateVerificationCode() {
        authorizedNumber = UUID.randomUUID().toString().replace("-", "").take(4)
        expiredAt = LocalDateTime.now().plusMinutes(5)
    }

    fun updateEmail(email: String) {
        emailAddress = email
    }
}
