package org.chewing.v1.dto.request

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.SignupRequest
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Email

data class EmailSignupRequest(
    val email: String,
    val phoneNumber: String,
    val countryCode: String,
    val firstName: String,
    val lastName: String,
    val birth: String,
    val verificationCode: String,
    val deviceId: String,
    val provider: String,
    val appToken: String
): SignupRequest {  // SignupRequest를 구현
    override fun toUser(): User {
        return User.generate(birth, firstName, lastName)
    }
    fun toPushToken(): PushToken {
        return PushToken.generate(appToken, provider, deviceId)
    }

    fun toEmail(): Email {
        return Email.authorize(email, verificationCode)
    }

}
