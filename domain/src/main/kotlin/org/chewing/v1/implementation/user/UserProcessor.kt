package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.StatusRemover
import org.chewing.v1.implementation.auth.AuthRemover
import org.chewing.v1.implementation.comment.CommentRemover
import org.chewing.v1.implementation.feed.FeedRemover
import org.chewing.v1.implementation.friend.FriendRemover
import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserProcessor(
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
    private val userRemover: UserRemover,
    private val statusRemover: StatusRemover,
    private val scheduleRemover: ScheduleRemover
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
}