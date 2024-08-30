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
        val user: User = userReader.readUserById(userId)
        val updatedUser: User = user.updateImage(file.name)
        imageProvider.removeImage(updatedUser.image.value())
        imageProvider.appendImage(file, updatedUser.image.value())
        return userUpdater.updateUser(updatedUser)
    }

    fun searchUser(keyword: String): User {
        return userReader.readUserByKeyword(keyword)
    }

    @Transactional
    fun changeStatusMessage(userId: User.UserId, statusMessage: String): User.UserId {
        val user: User = userReader.readUserById(userId)
        val updatedUser: User = user.updateStatusMessage(statusMessage)
        return userUpdater.updateUser(updatedUser)
    }
}