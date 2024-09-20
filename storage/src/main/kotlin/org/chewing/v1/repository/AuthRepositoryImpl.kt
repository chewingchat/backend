package org.chewing.v1.repository

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.jpaentity.AuthJpaEntity
import org.chewing.v1.jpaentity.EmailJpaEntity
import org.chewing.v1.jpaentity.LoggedInEntity
import org.chewing.v1.jpaentity.PhoneNumberJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
class AuthRepositoryImpl(
    private val authJpaRepository: AuthJpaRepository,
    private val confirmPhoneNumberJpaRepository: ConfirmPhoneNumberJpaRepository,
    private val loggedInJpaRepository: LoggedInJpaRepository,
    // 추가
    private val confirmEmailJpaRepository: ConfirmEmailJpaRepository,
    private val phoneNumberJpaRepository: PhoneNumberJpaRepository,
    private val emailJpaRepository: EmailJpaRepository,


    ) : AuthRepository {

    override fun checkEmailRegistered(emailAddress: String): Boolean {
        val authEntity = authJpaRepository.findUserByEmail(emailAddress)
        return authEntity.isPresent
        // && authEntity.get().phoneNumber.phoneNumber == phoneNumber
    }
    override fun checkPhoneRegistered(phoneNumber: String, countryCode: String): Boolean {
        val authEntity = authJpaRepository.findByPhoneNumber(phoneNumber, countryCode)
        return authEntity.isPresent
        // && authEntity.get().phoneNumber.phoneNumber == phoneNumber
    }



    override fun isEmailVerificationCodeValid(email: String, verificationCode: String): Boolean {
        return authJpaRepository.findByEmailAndVerificationCode(email, verificationCode).isPresent
    }
    override fun isPhoneVerificationCodeValid(phoneNumber: String, verificationCode: String): Boolean {
        return authJpaRepository.findByPhoneAndVerificationCode(phoneNumber, verificationCode).isPresent
    }
    override fun readEmail(email: String): Email? {
        val emailConfirmJpaEntity =
            confirmEmailJpaRepository.findWithEmail(email)
        return emailConfirmJpaEntity.map { it.toEmail() }.orElse(null)
    }


    override fun readPhoneNumber(phoneNumber: String, countryCode: String): Phone? {
        val phoneNumberConfirmJpaEntity =
            confirmPhoneNumberJpaRepository.findWithPhoneNumber(phoneNumber, countryCode)
        return phoneNumberConfirmJpaEntity.map { it.toPhone() }.orElse(null)
    }

    override fun readAuthInfoWithUser(phoneNumber: String): Pair<User, AuthInfo> {
        val authEntity = authJpaRepository.findAuthInfoByPhoneNumber(phoneNumber)
        val user = authEntity.map { it.toUser() }.orElse(null)
        val authInfo = authEntity.map { it.toAuthInfoOnlyWithId() }.orElse(null)
        return Pair(user, authInfo)
    }

    override fun readAuthInfoWithUserEmail(emailAddress: String): Pair<User, AuthInfo> {
        val authEntity = authJpaRepository.findAuthInfoByEmail(emailAddress)
        val user = authEntity.map { it.toUser() }.orElse(null)
        val authInfo = authEntity.map { it.toAuthInfoOnlyWithId() }.orElse(null)
        return Pair(user, authInfo)
    }



    override fun appendLoggedInInfo(authInfo: AuthInfo, refreshToken: RefreshToken) {
        loggedInJpaRepository.save(LoggedInEntity.fromAuthInfo(authInfo, refreshToken))
    }


    override fun savePhoneVerificationInfo(authInfo: AuthInfo) {
        // AuthInfo를 JPA 엔티티로 변환한 후 저장
        val authEntity = AuthJpaEntity.fromAuthInfo(authInfo)
        authJpaRepository.save(authEntity)
    }
    override fun saveEmailVerificationInfo(authInfo: AuthInfo) {
        // AuthInfo를 JPA 엔티티로 변환한 후 저장
        val authEntity = AuthJpaEntity.fromAuthInfo(authInfo)
        authJpaRepository.save(authEntity)
    }
    // 로그아웃 시 로그인 정보를 삭제하는 로직
    override fun deleteLoggedInInfo(userId: User.UserId) {
        // 로그인 정보를 찾아 삭제
        val authInfo = authJpaRepository.findByUserId(userId.value())
        val loggedInEntity = loggedInJpaRepository.findByAuthAuthId(authInfo.get().authId)
        loggedInJpaRepository.delete(loggedInEntity!!)
    }

    override fun deleteByUserId(userId: String) {
        authJpaRepository.deleteById(userId)
    }

    override fun readUserById(userId: String): User? {
        TODO("Not yet implemented")
    }


    //추가
    // 사용자 전화번호 업데이트

    override fun updateUserPhoneNumber(userId: String, phoneNumber: String, countryCode: String) {
        val phoneEntity = phoneNumberJpaRepository.findByUserId(userId)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }

        // 전화번호 업데이트
        val updatedPhoneEntity = PhoneNumberJpaEntity(
            phoneNumberId = phoneEntity.phoneNumberId,
            phoneNumber = phoneNumber,
            firstAuthorized = phoneEntity.firstAuthorized,
            countryCode = countryCode
        )
        phoneNumberJpaRepository.save(updatedPhoneEntity)
    }

    // 사용자 이메일 업데이트
    override fun updateUserEmail(userId: String, email: String) {
        val emailEntity = emailJpaRepository.findByUserId(userId)
            .orElseThrow { NotFoundException(ErrorCode.USER_NOT_FOUND) }

        // 이메일 업데이트
        val updatedEmailEntity = EmailJpaEntity(
            emailId = emailEntity.emailId,
            emailAddress = email,
            firstAuthorized = emailEntity.firstAuthorized
        )
        emailJpaRepository.save(updatedEmailEntity)
    }



}
