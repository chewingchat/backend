package org.chewing.v1.jparepository.auth

import org.chewing.v1.jpaentity.auth.PhoneJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
internal interface PhoneJpaRepository : JpaRepository<PhoneJpaEntity, String> {
    fun findByNumberAndCountryCode(phoneNumber: String, countryCode: String): Optional<PhoneJpaEntity>

    fun findByPhoneNumberId(phoneNumberId: String): Optional<PhoneJpaEntity>
}
