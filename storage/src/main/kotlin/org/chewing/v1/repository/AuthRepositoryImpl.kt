package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.*
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneNumberJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.model.auth.AuthInfo
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
internal class AuthRepositoryImpl(
    private val authJpaRepository: AuthJpaRepository,
    private val loggedInJpaRepository: LoggedInJpaRepository,
    private val phoneNumberJpaRepository: PhoneNumberJpaRepository,
    private val emailJpaRepository: EmailJpaRepository,


    ) : AuthRepository {
    override fun savePhoneVerification(phoneNumber: PhoneNumber): String {
        return phoneNumberJpaRepository.save(PhoneNumberJpaEntity.generate(phoneNumber)).getVerifiedNumber()
    }


    override fun saveEmailVerification(email: String): String {
        // AuthInfo를 JPA 엔티티로 변환한 후 저장
        return emailJpaRepository.save(EmailJpaEntity.generate(email)).getAuthorizedNumber()
    }


    override fun checkEmailRegistered(emailAddress: String): Boolean {
        return emailJpaRepository.existsByEmailAddressAndFirstAuthorizedTrue(emailAddress)
        // && authEntity.get().phoneNumber.phoneNumber == phoneNumber
    }

    override fun checkPhoneRegistered(phoneNumber: PhoneNumber): Boolean {
        return phoneNumberJpaRepository.existsByPhoneNumberAndCountryCodeAndFirstAuthorizedTrue(
            phoneNumber.number,
            phoneNumber.countryCode
        )
        // && authEntity.get().phoneNumber.phoneNumber == phoneNumber
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

    override fun readInfoByEmailId(emailId: String): AuthInfo? {
        return authJpaRepository.findByEmailId(emailId).orElse(null).toAuthInfo()
    }

    override fun readInfoByPhoneNumberId(phoneNumberId: String): AuthInfo? {
        return authJpaRepository.findByPhoneNumberId(phoneNumberId).orElse(null).toAuthInfo()
    }

    override fun readInfoByUserId(userId: String): AuthInfo? {
        return authJpaRepository.findByUserId(userId).orElse(null).toAuthInfo()
    }

    override fun saveAuthInfoByEmailId(emailId: String, userId: String): AuthInfo {
        return authJpaRepository.save(AuthJpaEntity.generateEmail(emailId, userId)).toAuthInfo()
    }

    override fun saveAuthInfoByPhoneNumberId(phoneNumberId: String, userId: String): AuthInfo {
        return authJpaRepository.save(AuthJpaEntity.generatePhoneNumber(phoneNumberId, userId)).toAuthInfo()
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

    override fun removeLoginInfo(authId: String) {
        loggedInJpaRepository.deleteByAuthId(authId)
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


    override fun appendLoggedInInfo(authInfo: AuthInfo, refreshToken: RefreshToken) {
        loggedInJpaRepository.save(LoggedInEntity.fromAuthInfo(authInfo, refreshToken))
    }
    override fun readByContact(contact: Any): AuthInfo? {
        return when (contact) {
            is Email -> {
                emailJpaRepository.findByEmailAddress(contact.emailAddress).orElse(null)?.toEmail()?.let {
                    readInfoByEmailId(it.emailId)
                }
            }
            is Phone -> {
                phoneNumberJpaRepository.findByPhoneNumberAndCountryCode(contact.number, contact.country).orElse(null)?.toPhone()?.let {
                    readInfoByPhoneNumberId(it.phoneId)
                }
            }
            else -> null
        }
    }
}
