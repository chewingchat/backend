package org.chewing.v1.dto.request

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.UserContent
import org.chewing.v1.model.contact.PhoneNumber
import org.springframework.web.multipart.MultipartFile

class SignupRequest {
    data class Email(
        val emailId: String,
        val deviceId: String,
        val provider: String,
        val pushToken: String,
        val firstName: String,
        val lastName: String,
        val birth: String,
        val profileImage: MultipartFile? // 프로필 이미지 추가
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
        val provider: String,
        val pushToken: String,
        val firstName: String,
        val lastName: String,
        val birth: String,
        val profileImage: MultipartFile? // 프로필 이미지 추가
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