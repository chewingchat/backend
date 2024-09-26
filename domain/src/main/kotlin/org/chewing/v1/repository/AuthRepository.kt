package org.chewing.v1.repository


import org.chewing.v1.model.user.User
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
    fun updatePhoneAuthorized(phoneId: String)
    fun updateEmailAuthorized(emailId: String)
    fun removeLoginInfo(userId: String)
    fun updateEmailVerificationCode(emailAddress: String): String
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String
    fun updateEmail(email: String)
    fun updatePhoneNumber(phoneNumber: PhoneNumber)
    fun appendLoggedInInfo(refreshToken: RefreshToken, user: User)
}