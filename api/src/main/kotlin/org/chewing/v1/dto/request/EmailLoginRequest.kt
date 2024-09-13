package org.chewing.v1.dto.request

import org.chewing.v1.model.Phone
import org.chewing.v1.model.PushToken
import org.chewing.v1.model.Email

data class EmailLoginRequest(
    val email: String,
    val phoneNumber: String,
    val verificationCode: String,
    val countryCode: String,
    val deviceId: String,
    val provider: String,
    val appToken: String
){
    fun toPushToken(): PushToken {
        return PushToken.generate(appToken, provider, deviceId)
    }

    fun toEmail(): Email {
        return Email.authorize(email, verificationCode)
    }
}
