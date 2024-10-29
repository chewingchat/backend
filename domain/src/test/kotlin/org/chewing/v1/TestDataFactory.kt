package org.chewing.v1

import org.chewing.v1.model.user.AccessStatus
import org.chewing.v1.model.announcement.Announcement
import org.chewing.v1.model.auth.*
import org.chewing.v1.model.chat.log.ChatFileLog
import org.chewing.v1.model.chat.log.ChatLogType
import org.chewing.v1.model.chat.log.ChatNormalLog
import org.chewing.v1.model.chat.message.*
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.model.chat.room.ChatSequenceNumber
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.model.user.*
import java.io.ByteArrayInputStream
import java.time.LocalDateTime

object TestDataFactory {

    fun createPhoneNumber(): PhoneNumber {
        return PhoneNumber.of("82", "1234567890")
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

    fun createUserAccount(emailId: String?, phoneId: String?): UserAccount {
        return UserAccount.of(createUser("userId"), emailId, phoneId)
    }

    fun createPhone(verificationCode: String): Phone {
        return Phone.of("testPhoneId", "82", "1234567890", verificationCode, LocalDateTime.now().plusMinutes(1))
    }

    fun createUserContent(): UserContent {
        return UserContent.of("firstName", "lastName", "2000-00-00")
    }

    fun createUserStatus(userId: String): UserStatus {
        return UserStatus.of("statusId", "statusMessage", "emoji", userId, true)
    }

    fun createDefaultUserStatus(): UserStatus {
        return UserStatus.default("userId")
    }

    fun createUserName(): UserName {
        return UserName.of("firstName", "lastName")
    }

    fun createProfileMedia(): Media {
        return Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG)
    }

    fun createMedia(category: FileCategory, index: Int, mediaType: MediaType): Media {
        return Media.of(category, "www.example.com", index, mediaType)
    }

    fun createFileData(
        contentType: MediaType = MediaType.IMAGE_JPEG,
        fileName: String = "test_image.jpg",
    ): FileData {
        val content = "Test file content"
        val size = content.toByteArray().size.toLong()
        val inputStream = ByteArrayInputStream(content.toByteArray())
        return FileData.of(inputStream, contentType, fileName, size)
    }

    fun createAppToken(): String {
        return "someAppToken"
    }

    fun createDevice(): PushToken.Device {
        return PushToken.Device.of("deviceId", PushToken.Provider.ANDROID)
    }

    fun createJwtToken(): JwtToken {
        return JwtToken.of("accessToken", RefreshToken.of("refreshToken", LocalDateTime.now()))
    }

    fun createUser(userId: String): User {
        return User.of(
            userId,
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

    fun createScheduledTime(): ScheduleTime {
        return ScheduleTime.of(LocalDateTime.now(), LocalDateTime.now().plusHours(1), LocalDateTime.now())
    }

    fun createScheduleContent(): ScheduleContent {
        return ScheduleContent.of("testTitle", "memo", "location")
    }

    fun createSchedule(): Schedule {
        return Schedule.of(
            "scheduleId",
            "title",
            "memo",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now(),
            "location"
        )
    }

    fun createFriendShip(friendId: String, accessStatus: AccessStatus): FriendShip {
        return FriendShip.of(friendId, createUserName(), true, accessStatus)
    }

    fun createCommentInfo(userId: String, commentId: String, feedId: String): CommentInfo {
        return CommentInfo.of(commentId, "comment", LocalDateTime.now(), userId, feedId)
    }

    fun createFeedInfo(feedId: String, userId: String): FeedInfo {
        return FeedInfo.of(feedId, "topic", 5, 5, LocalDateTime.now(), userId)
    }

    private fun createFeedMedia(index: Int): Media {
        return Media.of(FileCategory.FEED, "www.example.com", index, MediaType.IMAGE_PNG)
    }

    fun createFeedDetail(feedId: String, feedDetailId: String, index: Int): FeedDetail {
        return FeedDetail.of(feedDetailId, createFeedMedia(index), feedId)
    }

    fun createAnnouncement(announcementId: String): Announcement {
        return Announcement.of(announcementId, "title", LocalDateTime.now(), "content")
    }

    fun createUserSearch(userId: String): UserSearch {
        return UserSearch.of(userId, LocalDateTime.now())
    }

    fun createLoginInfo(user: User): LoginInfo {
        return LoginInfo.of(createJwtToken(), user)
    }

    fun createChatNormalLog(
        messageId: String,
        chatRoomId: String,
        userId: String,
        chatRoomNumber: ChatNumber
    ): ChatNormalLog {
        return ChatNormalLog.of(
            messageId,
            chatRoomId,
            userId,
            "text",
            chatRoomNumber,
            LocalDateTime.now(),
            ChatLogType.NORMAL
        )
    }

    fun createChatFileLog(
        messageId: String,
        chatRoomId: String,
        userId: String,
        chatRoomNumber: ChatNumber,
    ): ChatFileLog {
        return ChatFileLog.of(
            messageId,
            chatRoomId,
            userId,
            listOf(
                Media.of(FileCategory.CHAT, "www.example.com", 0, MediaType.IMAGE_PNG)
            ),
            LocalDateTime.now(),
            chatRoomNumber,
            ChatLogType.FILE,
        )
    }

    fun createChatSequenceNumber(chatRoomId: String): ChatSequenceNumber {
        return ChatSequenceNumber.of(1, chatRoomId)
    }

    fun createChatNumber(chatRoomId: String): ChatNumber {
        return ChatNumber.of(chatRoomId, 0, 0)
    }
}
