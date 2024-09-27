package org.chewing.v1.service

import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.UserProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.user.UserStatusFinder
import org.chewing.v1.model.User
import org.chewing.v1.model.UserStatus
import org.springframework.stereotype.Service
import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    private val userStatusFinder: UserStatusFinder
) {
    fun updateUserImage(file: File, userId: String) {
        val media = fileProcessor.processNewFile(userId, file)
        val preMedia = userProcessor.processChangeImage(userId, media)
        fileProcessor.processPreFile(preMedia)
    }
    //사용자의 통합된 정보를 가져옴
    fun fulledUser(userId: String): Pair<User, UserStatus> {
        val user = userReader.read(userId)
        val userStatus = userStatusFinder.find(userId)
        return Pair(user, userStatus)
    }
}