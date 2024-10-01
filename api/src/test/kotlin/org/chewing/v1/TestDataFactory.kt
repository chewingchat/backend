package org.chewing.v1

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.announcement.Announcement
import org.chewing.v1.model.auth.JwtToken
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.media.MediaType
import org.chewing.v1.model.schedule.Schedule
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
            Media.of(FileCategory.PROFILE, "www.example.com", 0, MediaType.IMAGE_PNG),
            Media.of(FileCategory.BACKGROUND, "www.example.com", 0, MediaType.IMAGE_PNG),
            AccessStatus.ACCESS
        )
    }


    fun createUserStatus(): UserStatus {
        return UserStatus.of("testStatusId", "testMessage", "testEmoji", "testUserId", true)
    }

    fun createFriend(): Friend {
        return Friend.of(createUser(), true, createFriendName(), createUserStatus(), AccessStatus.ACCESS)
    }

    fun createSchedule(): Schedule {
        return Schedule.of(
            "testScheduleId",
            "testScheduleTitle",
            "testScheduleMemo",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            "testLocation"
        )
    }

    fun createFeedDetail1(): FeedDetail {
        return FeedDetail.of(
            "testFeedDetailId",
            Media.of(FileCategory.FEED, "www.example.com", 0, MediaType.IMAGE_PNG),
            "feedId",
        )
    }

    fun createFeedInfo(): FeedInfo {
        return FeedInfo.of(
            "feedId",
            "testTopic",
            1,
            1,
            LocalDateTime.now(),
            "testUserId",
        )
    }

    fun createFeedDetail2(): FeedDetail {
        return FeedDetail.of(
            "feedDetailId",
            Media.of(FileCategory.FEED, "www.example.com", 0, MediaType.IMAGE_PNG),
            "feedId"
        )
    }

    fun createFeed(): Feed {
        return Feed.of(
            createFeedInfo(),
            listOf(createFeedDetail1(), createFeedDetail2())
        )
    }

    fun createComment(): Comment {
        return Comment.of("commentId", "comment", LocalDateTime.now(), createUser())
    }

    fun createAnnouncement(): Announcement {
        return Announcement.of("announcementId", "title", LocalDateTime.now(), "content")
    }
}
