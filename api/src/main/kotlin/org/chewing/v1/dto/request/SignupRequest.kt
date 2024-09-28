package org.chewing.v1.dto.request

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.UserContent

class SignupRequest {
    data class Email(
        val emailId: String,
        val deviceId: String,
        val provider: PushToken.Provider,
        val pushToken: String,
        val firstName: String,
        val lastName: String,
        val birth: String,
    ) {
        fun toEmailId(): String {
            return emailId
        }
        fun toDevice(): PushToken.Device {
            return PushToken.Device.of(deviceId, provider)
        }

        fun toPushToken(): String {
            return pushToken
        }
        fun toUserContent(): UserContent {
            return UserContent.of(firstName, lastName, birth)
        }
    }
    data class Phone(
        val phoneNumberId: String,
        val deviceId: String,
        val provider: PushToken.Provider,
        val pushToken: String,
        val firstName: String,
        val lastName: String,
        val birth: String,
    ) {
        fun toPhoneId(): String {
            return phoneNumberId
        }
        fun toDevice(): PushToken.Device {
            return PushToken.Device.of(deviceId, provider)
        }

        fun toPushToken(): String {
            return pushToken
        }
        fun toUserContent(): UserContent {
            return UserContent.of(firstName, lastName, birth)
        }
    }
}