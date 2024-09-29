package org.chewing.v1

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.user.UserStatus
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
        return "123456"
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

    fun createFriendName(): UserName {
        return UserName.of("testFriendFirstName", "testFriendLastName")
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


    fun createUserStatus(): UserStatus {
        return UserStatus.of("testStatusId", "testMessage", "testEmoji", "testUserId", true)
    }

    fun createFriend(): Friend {
        return Friend.of(createUser(), true, createFriendName(), createUserStatus(), AccessStatus.ACCESS)
    }
}
