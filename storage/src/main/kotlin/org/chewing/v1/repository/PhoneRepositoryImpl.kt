package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.auth.PhoneJpaEntity
import org.chewing.v1.jparepository.PhoneJpaRepository
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Phone
import org.springframework.stereotype.Repository

@Repository
internal class PhoneRepositoryImpl(
    private val phoneJpaRepository: PhoneJpaRepository
):PhoneRepository {
    override fun appendIfNotExists(phoneNumber: PhoneNumber){
        phoneJpaRepository.findByNumberAndCountryCode(
            phoneNumber.number,
            phoneNumber.countryCode
        ).orElseGet { phoneJpaRepository.save(PhoneJpaEntity.generate(phoneNumber)) }
    }

    override fun read(phoneNumber: PhoneNumber): Phone? {
        return phoneJpaRepository.findByNumberAndCountryCode(
            phoneNumber.number,
            phoneNumber.countryCode
        ).map { it.toPhone() }.orElse(null)
    }

    override fun updateVerificationCode(phoneNumber: PhoneNumber): String {
        val phoneEntity =
            phoneJpaRepository.findByNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
                .orElseThrow { NotFoundException(ErrorCode.PHONE_NUMBER_NOT_FOUND) }
        phoneEntity.updateVerificationCode()
        phoneJpaRepository.save(phoneEntity)
        return phoneEntity.getVerifiedNumber()
    }

    override fun readById(phoneNumberId: String): Phone? {
        return phoneJpaRepository.findByPhoneNumberId(phoneNumberId).map { it.toPhone() }.orElse(null)
    }
}