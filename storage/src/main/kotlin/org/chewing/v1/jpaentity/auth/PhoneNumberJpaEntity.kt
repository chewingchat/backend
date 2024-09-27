package org.chewing.v1.jpaentity.auth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.auth.PhoneNumber
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "phone_number", schema = "chewing")
internal class PhoneNumberJpaEntity(
    @Id
    @Column(name = "phone_number_id")
    private val phoneNumberId: String = UUID.randomUUID().toString(),
    @Column(name = "phone_number")
    private var phoneNumber: String,
    @Column(name = "first_authorized")
    private var firstAuthorized: Boolean,

    @Column(name = "country_code")
    private var countryCode: String,

    @Column(name = "expired_at")
    private var expiredAt: LocalDateTime = LocalDateTime.now().plusMinutes(5),

    @Column(name = "authorized_number")
    private var authorizedNumber: String = UUID.randomUUID().toString().replace("-", "").take(4),
    ) : BaseEntity(
) {
    companion object {
        fun generate(phoneNumber: PhoneNumber): PhoneNumberJpaEntity {
            return PhoneNumberJpaEntity(
                phoneNumber = phoneNumber.number,
                firstAuthorized = false,
                countryCode = phoneNumber.countryCode
            )
        }
    }
    fun toPhone(): Phone {
        return Phone.of(
            phoneId = phoneNumberId,
            number = phoneNumber,
            country = countryCode,
            authorizedNumber = authorizedNumber,
            expiredTime = expiredAt,
            isAuthorized = firstAuthorized,
        )
    }

    fun updateFirstAuthorized() {
        firstAuthorized = true
    }

    fun getVerifiedNumber(): String {
        return authorizedNumber
    }

    fun updateVerificationCode() {
        authorizedNumber = UUID.randomUUID().toString().replace("-", "").take(4)
        expiredAt = LocalDateTime.now().plusMinutes(5)
    }

    fun updatePhoneNumber(phoneNumber: PhoneNumber) {
        countryCode = phoneNumber.countryCode
        this.phoneNumber = phoneNumber.number
    }
}