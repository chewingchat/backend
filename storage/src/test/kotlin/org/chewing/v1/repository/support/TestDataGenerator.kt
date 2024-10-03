package org.chewing.v1.repository.support

import org.chewing.v1.jpaentity.announcement.AnnouncementJpaEntity
import org.chewing.v1.jpaentity.auth.EmailJpaEntity
import org.chewing.v1.jpaentity.auth.LoggedInJpaEntity
import org.chewing.v1.jpaentity.auth.PhoneJpaEntity
import org.chewing.v1.jpaentity.schedule.ScheduleJpaEntity
import org.chewing.v1.jpaentity.user.PushNotificationJpaEntity
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.user.UserStatusJpaEntity
import org.chewing.v1.jparepository.*
import org.chewing.v1.jparepository.AnnouncementJpaRepository
import org.chewing.v1.jparepository.EmailJpaRepository
import org.chewing.v1.jparepository.LoggedInJpaRepository
import org.chewing.v1.jparepository.PhoneJpaRepository
import org.chewing.v1.jparepository.ScheduleJpaRepository
import org.chewing.v1.model.announcement.Announcement
import org.chewing.v1.model.auth.EmailAddress
import org.chewing.v1.model.auth.PhoneNumber
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.contact.Email
import org.chewing.v1.model.contact.Phone
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

    fun scheduleEntityData(content: ScheduleContent, time: ScheduleTime, user: User): Schedule {
        val scheduleEntity = ScheduleJpaEntity.generate(content, time, user)
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
}