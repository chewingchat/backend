package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.User
import org.chewing.v1.model.UserName
import org.chewing.v1.model.UserStatus
import org.springframework.stereotype.Service
import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    private val userStatusFinder: UserStatusFinder,
    private val userUpdater: UserUpdater,
    private val feedReader: FeedReader
) {
    fun updateUserImage(file: File, userId: String) {
        val media = fileProcessor.processNewFile(userId, file)
        val preMedia = userProcessor.processChangeImage(userId, media)
        fileProcessor.processOldFile(preMedia)
    }

    //사용자의 통합된 정보를 가져옴
    fun fulledUser(userId: String): Pair<User, UserStatus> {
        val user = userReader.read(userId)
        val userStatus = userStatusFinder.find(userId)
        return Pair(user, userStatus)
    }

    fun updateUserName(userId: String, userName: UserName) {
        userUpdater.updateName(userId, userName)
    }

    fun deleteUser(userId: String) {
        val user = userReader.read(userId)
        val feeds = feedReader.readsByUser(userId)
        val feedDetails = feedReader.readsDetails(feeds.map { it.feedId })
        userProcessor.processRemoveUser(userId)
        fileProcessor.processOldFile(user.image)
        fileProcessor.processOldFiles(feedDetails.map { it.media })
    }
}