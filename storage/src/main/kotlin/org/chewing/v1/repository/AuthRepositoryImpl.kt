package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.*
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
internal class AuthRepositoryImpl(
    private val loggedInJpaRepository: LoggedInJpaRepository,
    private val phoneNumberJpaRepository: PhoneNumberJpaRepository,
    private val emailJpaRepository: EmailJpaRepository,


    ) : AuthRepository {
    override fun savePhoneVerification(phoneNumber: PhoneNumber): String {
        return phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
            .map {
                it.updateVerificationCode()
                phoneNumberJpaRepository.save(it).getVerifiedNumber()
            }
            .orElseGet {
                phoneNumberJpaRepository.save(PhoneNumberJpaEntity.generate(phoneNumber)).getVerifiedNumber()
            }
    }

    override fun saveEmailVerification(email: String): String {
        return emailJpaRepository.findByEmailAddress(email)
            .map {
                it.updateVerificationCode()
                emailJpaRepository.save(it).getAuthorizedNumber()
            }
            .orElseGet {
                emailJpaRepository.save(EmailJpaEntity.generate(email)).getAuthorizedNumber()
            }
    }

    override fun readEmail(email: String): Email? {
        val emailConfirmJpaEntity =
            emailJpaRepository.findByEmailAddress(email)
        return emailConfirmJpaEntity.map { it.toEmail() }.orElse(null)
    }


    override fun readPhoneNumber(phoneNumber: PhoneNumber): Phone? {
        val phoneNumberConfirmJpaEntity =
            phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
        return phoneNumberConfirmJpaEntity.map { it.toPhone() }.orElse(null)
    }

    override fun updateEmailAuthorized(emailId: String) {
        emailJpaRepository.findById(emailId).orElse(null).let {
            it.updateFirstAuthorized()
            emailJpaRepository.save(it)
        }
    }

    override fun updatePhoneAuthorized(phoneId: String) {
        phoneNumberJpaRepository.findById(phoneId).orElse(null).let {
            it.updateFirstAuthorized()
            phoneNumberJpaRepository.save(it)
        }
    }

    override fun removeLoginInfo(userId: String) {
        loggedInJpaRepository.deleteByUserId(userId)
    }

    override fun updateEmailVerificationCode(emailAddress: String): String {
        val emailEntity = emailJpaRepository.findByEmailAddress(emailAddress)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        emailEntity.updateVerificationCode()
        emailJpaRepository.save(emailEntity)
        return emailEntity.getAuthorizedNumber()
    }

    override fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String {
        val phoneEntity =
            phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
                .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        phoneEntity.updateVerificationCode()
        phoneNumberJpaRepository.save(phoneEntity)
        return phoneEntity.getVerifiedNumber()
    }

    override fun updatePhoneNumber(phoneNumber: PhoneNumber) {
        val phoneEntity =
            phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(phoneNumber.number, phoneNumber.countryCode)
                .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        phoneEntity.updatePhoneNumber(phoneNumber)
    }

    override fun updateEmail(email: String) {
        val emailEntity = emailJpaRepository.findByEmailAddress(email)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }
        emailEntity.updateEmail(email)
    }


    override fun appendLoggedInInfo(refreshToken: RefreshToken, user: User) {
        loggedInJpaRepository.save(LoggedInEntity.fromToken(refreshToken, user))
    }
}
