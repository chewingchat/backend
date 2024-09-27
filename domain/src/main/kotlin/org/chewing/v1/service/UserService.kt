package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.emoticon.EmoticonPack
import org.springframework.stereotype.Service
import java.io.File

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    private val userStatusFinder: UserStatusFinder,
    private val userUpdater: UserUpdater,
    private val feedReader: FeedReader,
    private val userEmoticonFinder: UserEmoticonFinder
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
    fun findOwnedEmoticonPacks(userId: String) : List<EmoticonPack>{
        return userEmoticonFinder.find(userId)
    }

    fun accessUser(
        userId: String,
        userContent: UserContent,
    ) {
        userUpdater.updateContent(userId, userContent)
    }
}