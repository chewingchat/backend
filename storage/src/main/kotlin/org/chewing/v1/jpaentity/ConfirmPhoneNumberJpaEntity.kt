package org.chewing.v1.jpaentity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

class ConfirmPhoneNumberJpaEntity(
    @Id
    @Column(name = "confirm_email_id")
    val confirmEmailId: String = UUID.randomUUID().toString(),

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime,

    @Column(name = "authorized_number")
    val authorizedNumber: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email_id", nullable = false)
    val phoneNumber: PhoneNumberJpaEntity
) {
}