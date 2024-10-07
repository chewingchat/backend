package org.chewing.v1

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.auth.*
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.model.user.*
import java.io.ByteArrayInputStream
import java.time.LocalDateTime

object TestDataFactory {

    fun createPhoneNumber(): PhoneNumber {
        return PhoneNumber.of("82", "1234567890")
    }

    fun createPushToken(): PushToken {
        return PushToken.of("pushTokenId", "appToken", PushToken.Provider.FCM, "deviceId")
    }

    fun createEmailAddress(): EmailAddress {
        return EmailAddress.of("test@exampl.com")
    }

    fun createEmail(verificationCode: String): Email {
        return Email.of("testEmailId", "test@exampl.com", verificationCode, LocalDateTime.now().plusMinutes(1))
    }

    fun createExpiredEmail(verificationCode: String): Email {
        return Email.of("testEmailId", "test@example.com", verificationCode, LocalDateTime.now().minusMinutes(1))
    }

    fun createUserAccount(): UserAccount {
        return UserAccount.of(createUser(), "testFirstName", "testLastName")
    }

    fun createPhone(verificationCode: String): Phone {
        return Phone.of("testPhoneId", "82", "1234567890", verificationCode, LocalDateTime.now().plusMinutes(1))
    }

    fun createUserContent(): UserContent {
        return UserContent.of("firstName", "lastName", "2000-00-00")
    }

    fun createUserStatus(): UserStatus {
        return UserStatus.of("statusId", "statusMessage", "emoji", "userId", true)
    }

    fun createUserName(): UserName {
        return UserName.of("firstName", "lastName")
    }

    fun createMedia(): Media {
        return Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG)
    }

    fun createFileData(
        content: String = "Test file content",
        contentType: MediaType = MediaType.IMAGE_JPEG,
        fileName: String = "test_image.jpg",
        size: Long = content.toByteArray().size.toLong()
    ): FileData {
        val inputStream = ByteArrayInputStream(content.toByteArray())
        return FileData.of(inputStream, contentType, fileName, size)
    }

    fun createRefreshToken(): RefreshToken {
        return RefreshToken.of("refreshToken", LocalDateTime.now())
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
            Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG),
            Media.of(FileCategory.BACKGROUND, "www.example.com", 0, MediaType.IMAGE_PNG),
            AccessStatus.ACCESS
        )
    }

    fun createNotAccessUser(): User {
        return User.of(
            "testUserId",
            "testFirstName",
            "testLastName",
            "2000-00-00",
            Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG),
            Media.of(FileCategory.BACKGROUND, "www.example.com", 0, MediaType.IMAGE_PNG),
            AccessStatus.NOT_ACCESS
        )
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
