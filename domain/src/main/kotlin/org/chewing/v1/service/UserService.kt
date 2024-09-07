package org.chewing.v1.service

import org.chewing.v1.implementation.ImageProvider
import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.UserUpdater
import org.chewing.v1.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val userUpdater: UserUpdater,
    private val imageProvider: ImageProvider,
) {
    @Transactional
    fun updateUserImage(file: File, userId: User.UserId): User.UserId {
        val user: User = userReader.readUser(userId)
        val updatedUser: User = user.updateImage(file.name)
        imageProvider.removeImage(updatedUser.image.url)
        imageProvider.appendImage(file, updatedUser.image.url)
        return userUpdater.updateUser(updatedUser)
    }
}