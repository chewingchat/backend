package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Locale.IsoCountryCode
import java.util.Optional

@Repository
internal interface PhoneNumberJpaRepository : JpaRepository<PhoneNumberJpaEntity, String> {
    fun existsByPhoneNumberAndCountryCodeAndFirstAuthorizedTrue(phoneNumber: String, countryCode: String): Boolean

    fun findByPhoneNumberAndCountryCode(phoneNumber: String, countryCode: String): Optional<PhoneNumberJpaEntity>
}