package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.repository.AuthRepository
import org.springframework.stereotype.Component

@Component
class AuthReader(
    private val authRepository: AuthRepository
) {
    //존재 하지 않는 휴대폰 -> 생길 가능성 없음(그래도 혹시 모르니)
    fun readPhoneNumber(phoneNumber: String,countryCode:String): Phone {
        return authRepository.readPhoneNumber(phoneNumber,countryCode) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    fun readInfoWithUserByPhoneNumber(phoneNumber: String): Pair<User,AuthInfo> {
        return authRepository.readAuthInfoWithUser(phoneNumber)
    }

    // 보완한 코드


    //존재 하지 않는 이메일
    fun readEmail(emailId: String): Email {
        return authRepository.readEmail(emailId) ?: throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    //이메일로 정보읽어오기
    fun readInfoWithUserByEmail(emailAddress: String): Pair<User,AuthInfo> {
        return authRepository.readAuthInfoWithUserEmail(emailAddress)
    }


}