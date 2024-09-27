package org.chewing.v1.repository


import org.chewing.v1.model.auth.AuthInfo
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.contact.PhoneNumber
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository {
    fun readPhoneNumber(phoneNumber: PhoneNumber): Phone?
    fun readEmail(email: String): Email?
    fun savePhoneVerification(phoneNumber: PhoneNumber): String
    fun saveEmailVerification(email: String): String
    fun readInfoByEmailId(emailId: String): AuthInfo?
    fun readInfoByPhoneNumberId(phoneNumberId: String): AuthInfo?
    fun readInfoByUserId(userId: String): AuthInfo?
    fun saveAuthInfoByEmailId(emailId: String, userId: String): AuthInfo
    fun saveAuthInfoByPhoneNumberId(phoneNumberId: String, userId: String): AuthInfo
    fun updatePhoneAuthorized(phoneId: String)
    fun updateEmailAuthorized(emailId: String)
    fun removeLoginInfo(authId: String)
    fun checkPhoneRegistered(phoneNumber: PhoneNumber): Boolean
    fun checkEmailRegistered(emailAddress: String): Boolean
    fun updateEmailVerificationCode(emailAddress: String): String
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String
    fun updateEmail(email: String)
    fun updatePhoneNumber(phoneNumber: PhoneNumber)
    fun appendLoggedInInfo(authInfo: AuthInfo, refreshToken: RefreshToken)
    fun readByContact(contact: Any): AuthInfo?
}