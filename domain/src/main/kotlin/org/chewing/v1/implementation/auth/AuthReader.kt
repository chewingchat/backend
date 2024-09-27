package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.auth.AuthInfo
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component


@Component
class AuthReader(
    private val authRepository: AuthRepository,
) {
    //존재 하지 않는 휴대폰 -> 생길 가능성 없음(그래도 혹시 모르니)
    fun readPhoneNumber(phoneNumber: PhoneNumber): Phone {
        return authRepository.readPhoneNumber(phoneNumber) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    fun readInfoByPhoneNumberId(phoneNumber: String): AuthInfo {
        return authRepository.readInfoByPhoneNumberId(phoneNumber)?:throw NotFoundException(ErrorCode.LONGIN_FAILED)
    }

    fun readEmail(email: String): Email {
        return authRepository.readEmail(email) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    //이메일로 정보읽어오기
    fun readInfoByEmailId(emailId: String): AuthInfo {
        return authRepository.readInfoByEmailId(emailId) ?: throw NotFoundException(ErrorCode.LONGIN_FAILED)
    }

    fun readInfoByUserId(userId: String): AuthInfo {
        return authRepository.readInfoByUserId(userId) ?: throw NotFoundException(ErrorCode.LONGIN_FAILED)
    }
    fun readByContact(contact: Any): AuthInfo? {
        return authRepository.readByContact(contact)
    }
}