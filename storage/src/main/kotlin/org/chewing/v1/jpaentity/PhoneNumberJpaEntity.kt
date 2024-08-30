package org.chewing.v1.jpaentity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.chewing.v1.common.BaseEntity
import java.util.*

@Entity
@Table(name = "phone_number", schema = "chewing")
class PhoneNumberJpaEntity(
    @Id
    @Column(name = "phone_number_id")
    val phoneNumberId: String = UUID.randomUUID().toString(),
    @Column(name = "phone_number")
    val phoneNumber: String,
    @Column(name = "first_authorized")
    val firstAuthorized: Boolean,
    @Column(name = "country_code")
    val countryCode: String
) : BaseEntity() {
}