package org.chewing.v1.implementation.user

import org.chewing.v1.model.User
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserProcessor(
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
) {
    @Transactional
    fun processChangeUserImage(userId: User.UserId, media: Media): Media {
        val user = userReader.readUser(userId)
        val updatedUser = user.updateImage(media)
        userUpdater.updateUser(updatedUser)
        return user.image
    }
}