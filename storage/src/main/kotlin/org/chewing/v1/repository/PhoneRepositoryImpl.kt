package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.chewing.v1.jparepository.PhoneJpaRepository
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Phone
import org.springframework.stereotype.Repository

@Repository
internal class PhoneRepositoryImpl(
    private val phoneJpaRepository: PhoneJpaRepository
):PhoneRepository {
    override fun savePhoneNumberIfNotExists(phoneNumber: PhoneNumber){
        phoneJpaRepository.findByPhoneNumberAndCountryCode(
            phoneNumber.number,
            phoneNumber.countryCode
        ).orElseGet { phoneJpaRepository.save(PhoneNumberJpaEntity.generate(phoneNumber)) }
    }

    override fun readPhone(phoneNumber: PhoneNumber): Phone? {
        return phoneJpaRepository.findByPhoneNumberAndCountryCode(
            phoneNumber.number,
            phoneNumber.countryCode
        ).map { it.toPhone() }.orElse(null)
    }

    override fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String {
        val phoneEntity =
            phoneJpaRepository.findByPhoneNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
                .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        phoneEntity.updateVerificationCode()
        phoneJpaRepository.save(phoneEntity)
        return phoneEntity.getVerifiedNumber()
    }

    override fun readPhoneByPhoneNumberId(phoneNumberId: String): Phone? {
        return phoneJpaRepository.findByPhoneNumberId(phoneNumberId).map { it.toPhone() }.orElse(null)
    }
}