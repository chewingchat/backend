package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
internal interface PhoneJpaRepository : JpaRepository<PhoneNumberJpaEntity, String> {
    fun findByPhoneNumberAndCountryCode(phoneNumber: String, countryCode: String): Optional<PhoneNumberJpaEntity>

    fun findByPhoneNumberId(phoneNumberId: String): Optional<PhoneNumberJpaEntity>
}