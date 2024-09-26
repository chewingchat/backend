package org.chewing.v1.dto.request

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.contact.PhoneNumber

class LoginRequest {
    data class Email(
        val email: String,
        val verificationCode: String,
        val deviceId: String,
        val provider: String,
        val appToken: String
    ){
        fun toDevice(): PushToken.Device {
            return PushToken.Device.of(deviceId, provider)
        }

        fun toAppToken(): String {
            return appToken
        }

        fun toAddress(): String {
            return email
        }

        fun toVerificationCode(): String {
            return verificationCode
        }
    }
    data class Phone(
        val phoneNumber: String,
        val countryCode: String,
        val verificationCode: String,
        val deviceId: String,
        val provider: String,
        val appToken: String
    ) {
        fun toDevice(): PushToken.Device {
            return PushToken.Device.of(deviceId, provider)
        }

        fun toAppToken(): String {
            return appToken
        }

        fun toVerificationCode(): String {
            return verificationCode
        }
        fun toPhoneNumber(): PhoneNumber {
            return PhoneNumber.of(countryCode, phoneNumber)
        }
    }
}