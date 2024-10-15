package org.chewing.v1.repository.support

import org.chewing.v1.jpaentity.announcement.AnnouncementJpaEntity
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.LoggedInJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneJpaEntity
import org.chewing.v1.jpaentity.feed.FeedCommentJpaEntity
import org.chewing.v1.jpaentity.feed.FeedDetailJpaEntity
import org.chewing.v1.jpaentity.feed.FeedJpaEntity
import org.chewing.v1.jpaentity.friend.FriendShipJpaEntity
import org.chewing.v1.jpaentity.user.ScheduleJpaEntity
import org.chewing.v1.jpaentity.user.PushNotificationJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.jparepository.announcement.AnnouncementJpaRepository
import org.chewing.v1.jparepository.auth.EmailJpaRepository
import org.chewing.v1.jparepository.auth.LoggedInJpaRepository
import org.chewing.v1.jparepository.auth.PhoneJpaRepository
import org.chewing.v1.jparepository.user.ScheduleJpaRepository
import org.chewing.v1.jparepository.feed.FeedCommentJpaRepository
import org.chewing.v1.jparepository.feed.FeedDetailJpaRepository
import org.chewing.v1.jparepository.feed.FeedJpaRepository
import org.chewing.v1.jparepository.friend.FriendShipJpaRepository
import org.chewing.v1.jparepository.user.PushNotificationJpaRepository
import org.chewing.v1.jparepository.user.UserJpaRepository
import org.chewing.v1.jparepository.user.UserStatusJpaRepository
import org.chewing.v1.model.user.AccessStatus
import org.chewing.v1.model.announcement.Announcement
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import org.chewing.v1.model.token.RefreshToken
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestDataGenerator(
) {
    @Autowired
    private lateinit var emailJpaRepository: EmailJpaRepository

    @Autowired
    private lateinit var phoneJpaRepository: PhoneJpaRepository

    @Autowired
    private lateinit var loggedInJpaRepository: LoggedInJpaRepository

    @Autowired
    private lateinit var announcementJpaRepository: AnnouncementJpaRepository

    @Autowired
    private lateinit var scheduleJpaRepository: ScheduleJpaRepository

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    private lateinit var userStatusJpaRepository: UserStatusJpaRepository

    @Autowired
    private lateinit var pushNotificationJpaRepository: PushNotificationJpaRepository

    @Autowired
    private lateinit var feedJpaRepository: FeedJpaRepository

    @Autowired
    private lateinit var feedDetailJpaRepository: FeedDetailJpaRepository

    @Autowired
    private lateinit var commentJpaRepository: FeedCommentJpaRepository

    @Autowired
    private lateinit var friendShipJpaRepository: FriendShipJpaRepository

    fun emailEntityData(emailAddress: EmailAddress): Email {
        val email = EmailJpaEntity.generate(emailAddress)
        emailJpaRepository.save(email)
        return email.toEmail()
    }

    fun phoneEntityData(phoneNumber: PhoneNumber): Phone {
        val phone = PhoneJpaEntity.generate(phoneNumber)
        phoneJpaRepository.save(phone)
        return phone.toPhone()
    }

    fun loggedInEntityData(refreshToken: RefreshToken) {
        val loggedIn = LoggedInJpaEntity.generate(refreshToken, "userId")
        loggedInJpaRepository.save(loggedIn)
    }

    fun announcementEntityData(): Announcement {
        val announcement = AnnouncementJpaEntity.generate("title", "content")
        announcementJpaRepository.save(announcement)
        return announcement.toAnnouncement()
    }

    fun announcementEntityDataList() {
        (1..10).map {
            AnnouncementJpaEntity.generate("title $it", "content $it")
        }.forEach { announcement ->
            announcementJpaRepository.save(announcement)
        }
    }

    fun scheduleEntityData(content: ScheduleContent, time: ScheduleTime, userId: String): Schedule {
        val scheduleEntity = ScheduleJpaEntity.generate(content, time, userId)
        scheduleJpaRepository.save(scheduleEntity)
        return scheduleEntity.toSchedule()
    }

    fun userEntityEmailData(email: Email): User {
        val user = UserJpaEntity.generateByEmail(email)
        userJpaRepository.save(user)
        return user.toUser()
    }

    fun userEntityPhoneData(phone: Phone): User {
        val user = UserJpaEntity.generateByPhone(phone)
        userJpaRepository.save(user)
        return user.toUser()
    }

    fun userStatusData(userId: String): UserStatus {
        UserStatusProvider.buildNotSelected(userId).let {
            val statusEntity = UserStatusJpaEntity.generate(
                it.userId,
                it.message,
                it.emoji
            )
            userStatusJpaRepository.save(statusEntity)
            return statusEntity.toUserStatus()
        }
    }

    fun userSelectedStatusData(userId: String): UserStatus {
        UserStatusProvider.buildSelected(userId).let {
            val statusEntity = UserStatusJpaEntity.generate(
                it.userId,
                it.message,
                it.emoji
            )
            statusEntity.updateSelectedTrue()
            userStatusJpaRepository.save(statusEntity)
            return statusEntity.toUserStatus()
        }
    }

    fun userStatusDataList(userId: String) {
        (1..10).map {
            UserStatusProvider.buildNotSelected(userId)
        }.map { userStatus ->
            UserStatusJpaEntity.generate(
                userStatus.userId,
                userStatus.message,
                userStatus.emoji
            )
        }.forEach { statusEntity ->
            userStatusJpaRepository.save(statusEntity)
        }
    }

    fun pushNotificationData(userId: String): PushToken {
        val user = UserProvider.buildNormal(userId)
        val device = PushTokenProvider.buildDeviceNormal()
        val appToken = PushTokenProvider.buildAppTokenNormal()
        return pushNotificationJpaRepository.save(
            PushNotificationJpaEntity.generate(
                appToken,
                device,
                user
            )
        ).toPushToken()
    }

    fun feedEntityData(userId: String): FeedInfo {
        return feedJpaRepository.save(FeedJpaEntity.generate("topic", userId)).toFeedInfo()
    }

    fun feedEntityDataList(userId: String): List<FeedInfo> {
        val feedJpaEntityList = (1..10).map {
            FeedJpaEntity.generate("topic $it", userId)
        }
        feedJpaRepository.saveAll(feedJpaEntityList)
        return feedJpaEntityList.map { it.toFeedInfo() }
    }

    fun feedDetailEntityDataAsc(feedId: String): List<FeedDetail> {
        val medias = MediaProvider.buildFeedContents()
        val feedEntities = FeedDetailJpaEntity.generate(medias, feedId)
        feedDetailJpaRepository.saveAll(feedEntities)
        return feedEntities.map { it.toFeedDetail() }
    }

    fun feedCommentEntityDataList(userId: String, feedId: String): List<CommentInfo> {
        (1..10).map {
            FeedCommentJpaEntity.generate(userId, feedId, "comment")
        }.let { commentEntities ->
            commentJpaRepository.saveAll(commentEntities)
            return commentEntities.map { it.toCommentInfo() }
        }
    }

    fun feedCommentEntityData(userId: String, feedId: String): CommentInfo {
        return commentJpaRepository.save(FeedCommentJpaEntity.generate(userId, feedId, "comment")).toCommentInfo()
    }

    fun friendShipEntityData(userId: String, friendId: String, access: AccessStatus) {
        val friendName = UserProvider.buildFriendName()
        val entity = FriendShipJpaEntity.generate(userId, friendId, friendName)
        when(access) {
            AccessStatus.DELETE -> entity.updateDelete()
            AccessStatus.BLOCK -> entity.updateBlock()
            AccessStatus.BLOCKED -> entity.updateBlocked()
            else -> {}
        }
        friendShipJpaRepository.save(entity)
    }
}