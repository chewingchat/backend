package org.chewing.v1.implementation.user

import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserProcessor(
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
) {
    @Transactional
    fun processChangeImage(userId: String, media: Media): Media {
        val user = userReader.read(userId)
        userUpdater.updateUser(user, media)
        return user.image
    }
}