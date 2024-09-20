package org.chewing.v1.repository


import org.chewing.v1.model.AuthInfo
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository {
    fun checkEmailRegistered(emailAddress: String): Boolean
    fun checkPhoneRegistered(phoneNumber: String, countryCode: String): Boolean
    fun isEmailVerificationCodeValid(email: String, verificationCode: String): Boolean
    fun isPhoneVerificationCodeValid(phoneNumber: String, verificationCode: String): Boolean

    fun readPhoneNumber(phoneNumber: String, countryCode: String): Phone?
    fun readAuthInfoWithUser(phoneNumber: String): Pair<User, AuthInfo>
    fun readAuthInfoWithUserEmail(emailAddress: String): Pair<User, AuthInfo>



    fun appendLoggedInInfo(authInfo: AuthInfo, refreshToken: RefreshToken)
    // 추가
    fun readEmail(email: String): Email?
    fun savePhoneVerificationInfo(authInfo: AuthInfo)
    fun saveEmailVerificationInfo(authInfo: AuthInfo)
    fun deleteLoggedInInfo(userId: User.UserId)
    // 추가
    fun deleteByUserId(userId: String)
    fun readUserById(userId: String): User?


    // 사용자 전화번호 업데이트
    fun updateUserPhoneNumber(userId: String, phoneNumber: String, countryCode: String)

    // 사용자 이메일 업데이트
    fun updateUserEmail(userId: String, email: String)


}