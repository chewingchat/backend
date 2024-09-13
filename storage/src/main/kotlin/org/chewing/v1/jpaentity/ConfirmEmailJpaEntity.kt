package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.Email
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime
import java.util.*

@Entity

@Table(name = "confirm_email", schema = "chewing")
class ConfirmEmailJpaEntity(
    @Id
    @Column(name = "confirm_email_id")
    val confirmEmailId: String = UUID.randomUUID().toString(),

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime,

    @Column(name = "authorized_number")
    val authorizedNumber: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email_id", nullable = false)
    val email: EmailJpaEntity
) { // ConfirmEmailJpaEntity를 Email 모델로 변환하는 메서드
    fun toEmail(): Email {

        return Email.of(
            email.emailId,  // Email 모델에 필요한 필드
            email.emailAddress,
            authorizedNumber,  // 인증번호
            expiredAt,  // 만료 시간
            email.firstAuthorized
        )
    }
}