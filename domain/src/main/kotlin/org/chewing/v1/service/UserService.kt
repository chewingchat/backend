package org.chewing.v1.service

import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.springframework.stereotype.Service
import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor
) {
    fun updateUserImage(file: File, userId: User.UserId) {
        val media = fileProcessor.processNewFile(userId, file)
        val preMedia = userProcessor.processChangeUserImage(userId, media)
        fileProcessor.processPreFile(preMedia)
    }
    fun getUserInfo(userId: User.UserId): User {
        return userReader.readUserWithStatus(userId)
    }
}