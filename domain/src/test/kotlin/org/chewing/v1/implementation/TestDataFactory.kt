package org.chewing.v1.implementation

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.auth.*
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.model.user.User
import java.time.LocalDateTime

object TestDataFactory {

    fun createPhoneNumber(): PhoneNumber {
        return PhoneNumber.of("82", "1234567890")
    }

    fun createPushToken(): PushToken {
        return PushToken.of("pushTokenId", "appToken", PushToken.Provider.FCM, "deviceId")
    }

    fun createEmailAddress(): String {
        return "test@example.com"
    }

    fun createVerificationCode(): String {
        return "testCode"
    }

    fun createWrongVerificationCode(): String {
        return "wrongCode"
    }

    fun createAppToken(): String {
        return "someAppToken"
    }

    fun createDevice(): PushToken.Device {
        return PushToken.Device.of("deviceId", PushToken.Provider.FCM)
    }

    fun createJwtToken(): JwtToken {
        return JwtToken.of("accessToken", RefreshToken.of("refreshToken", LocalDateTime.now()))
    }

    fun createUser(): User {
        return User.of(
            "testUserId",
            "testFirstName",
            "testLastName",
            "2000-00-00",
            Image.of("www.example.com", 0, MediaType.IMAGE_PNG),
            Image.of("www.example.com", 0, MediaType.IMAGE_PNG),
            AccessStatus.ACCESS
        )
    }

    fun createEmailCredential(): Credential {
        return EmailAddress.of("test@example.com")
    }

    fun createPhoneNumberCredential(): Credential {
        return PhoneNumber.of("82", "1234567890")
    }

    fun createValidEmailContact(): Email {
        return Email.of("testEmailId", "test@Example.com", "testCode", LocalDateTime.now().plusMinutes(1))
    }

    fun createValidPhoneContact(): Phone {
        return Phone.of("testPhoneId", "82", "1234567890", "testCode", LocalDateTime.now().plusMinutes(1))
    }

    fun createExpiredEmailContact(): Email {
        return Email.of("testEmailId", "test@Example.com", "testCode", LocalDateTime.now().minusMinutes(1))
    }

    fun createExpiredPhoneContact(): Phone {
        return Phone.of("testPhoneId", "82", "1234567890", "testCode", LocalDateTime.now().minusMinutes(1))
    }
}