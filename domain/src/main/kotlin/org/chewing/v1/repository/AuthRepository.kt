package org.chewing.v1.repository


import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.user.User
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.token.RefreshToken
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository {
    fun readContact(credential: Credential): Contact?
    fun saveCredentialIfNotExists(credential: Credential)
    fun removeLoginInfo(userId: String)
    fun updateEmailVerificationCode(emailAddress: EmailAddress): String
    fun updatePhoneVerificationCode(phoneNumber: PhoneNumber): String
    fun updateVerificationCode(credential: Credential):String
    fun appendLoggedIn(refreshToken: RefreshToken, user: User)
}