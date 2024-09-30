package org.chewing.v1.repository

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.*
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
internal class AuthRepositoryImpl(
    private val loggedInJpaRepository: LoggedInJpaRepository,
    private val phoneNumberJpaRepository: PhoneNumberJpaRepository,
    private val emailJpaRepository: EmailJpaRepository,


    ) : AuthRepository {
    override fun saveCredentialIfNotExists(credential: Credential) {
        when (credential) {
            is EmailAddress -> emailJpaRepository.findByEmailAddress(credential.email)
                .orElseGet { emailJpaRepository.save(EmailJpaEntity.generate(credential)) }

            is PhoneNumber -> phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(
                credential.number,
                credential.countryCode
            )
                .orElseGet { phoneNumberJpaRepository.save(PhoneNumberJpaEntity.generate(credential)) }

            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun readContact(credential: Credential): Contact? {
        return when (credential) {
            is EmailAddress -> emailJpaRepository.findByEmailAddress(credential.email).map { it.toEmail() }.orElse(null)
            is PhoneNumber -> phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(
                credential.number,
                credential.countryCode
            )
                .map { it.toPhone() }.orElse(null)

            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun removeLoginInfo(userId: String) {
        loggedInJpaRepository.deleteByUserId(userId)
    }

    override fun updateEmailVerificationCode(emailAddress: EmailAddress): String {
        val emailEntity = emailJpaRepository.findByEmailAddress(emailAddress.email)
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

    override fun generateVerificationCode(credential: Credential): String {
        return when (credential) {
            is EmailAddress -> updateEmailVerificationCode(credential)
            is PhoneNumber -> updatePhoneVerificationCode(credential)
            else -> throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    override fun appendLoggedIn(refreshToken: RefreshToken, loggedInId: String) {
        loggedInJpaRepository.findById(loggedInId).ifPresent {
            it.updateRefreshToken(refreshToken)
            loggedInJpaRepository.save(it)
        }
    }

    override fun readLoggedId(refreshToken: String): String? {
        return loggedInJpaRepository.findByRefreshToken(refreshToken).orElse(null).toLoggedInId()
    }

    override fun readContactByEmailId(emailId: String): Email? {
        return emailJpaRepository.findByEmailId(emailId).map { it.toEmail() }.orElse(null)
    }

    override fun readContactByPhoneNumberId(phoneNumberId: String): Phone? {
        return phoneNumberJpaRepository.findByPhoneNumberId(phoneNumberId).map { it.toPhone() }.orElse(null)
    }
}
