package org.chewing.v1.repository


import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.user.User
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository {
    fun readPhoneNumber(phoneNumber: PhoneNumber): Phone?
    fun readEmail(email: String): Email?
    fun readCredential(credential: Credential): Contact?
    fun savePhoneIfNotExists(phoneNumber: PhoneNumber)
    fun saveEmailIfNotExists(email: String)
    fun removeLoginInfo(userId: String)
    fun updateEmailVerificationCode(emailAddress: String): String
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String
    fun updateEmail(email: String)
    fun updatePhoneNumber(phoneNumber: PhoneNumber)
    fun appendLoggedIn(refreshToken: RefreshToken, user: User)
}