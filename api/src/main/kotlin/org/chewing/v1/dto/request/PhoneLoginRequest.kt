package org.chewing.v1.dto.request

import org.chewing.v1.model.Phone
import org.chewing.v1.model.PushToken


data class PhoneLoginRequest(
    val phoneNumber: String,
    val verificationCode: String,
    val countryCode: String,
    val deviceId: String,
    val provider: String,
    val appToken: String
) {
    fun toPushToken(): PushToken {
        return PushToken.generate(appToken, provider, deviceId)
    }

    fun toPhone(): Phone {
        return Phone.authorize(phoneNumber, countryCode, verificationCode)
    }
}