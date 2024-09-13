package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.Phone
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime
import java.util.*


@Entity

@Table(name = "confirm_phone_number", schema = "chewing")
class ConfirmPhoneNumberJpaEntity(
    @Id
    @Column(name = "confirm_phoneNumber_id")
    val confirmPhoneNumberId: String = UUID.randomUUID().toString(),

    @Column(name = "expired_at")
    val expiredAt: LocalDateTime,

    @Column(name = "authorized_number")
    val authorizedNumber: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email_id", nullable = false)
    val phoneNumber: PhoneNumberJpaEntity
) {
    fun toPhone(): Phone {
        return Phone.of(
            phoneNumber.phoneNumberId,
            phoneNumber.countryCode,
            phoneNumber.phoneNumber,
            authorizedNumber,
            expiredAt,
            phoneNumber.firstAuthorized
        )
    }
}