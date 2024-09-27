package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.user.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserProcessor(
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
    private val userRemover: UserRemover,
    private val statusRemover: StatusRemover,
    private val scheduleRemover: ScheduleRemover,
    private val userAppender: UserAppender
) {
    @Transactional
    fun processChangeImage(userId: String, media: Media): Media {
        val user = userReader.read(userId)
        userUpdater.updateProfileImage(user, media)
        return user.image
    }

    fun processRemoveUser(userId: String) {
        userRemover.remove(userId)
        statusRemover.removeAll(userId)
        scheduleRemover.removeAll(userId)
    }

    fun processPushToken(
        user: User, appToken: String,
        device: PushToken.Device
    ) {
        userRemover.removePushToken(device)
        userAppender.appendUserPushToken(user, appToken, device)
    }
}