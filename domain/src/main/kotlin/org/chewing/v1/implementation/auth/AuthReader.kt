package org.chewing.v1.implementation.auth

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
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
    fun readEmail(email: String): Email {
        return authRepository.readEmail(email) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }
    fun readCredential(credential: Credential): Contact {
        return authRepository.readCredential(credential) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}