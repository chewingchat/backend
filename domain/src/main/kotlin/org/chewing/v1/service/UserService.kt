package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    private val userStatusFinder: UserStatusFinder,
    private val userUpdater: UserUpdater,
    private val feedReader: FeedReader,
    private val userEmoticonFinder: UserEmoticonFinder,
    private val userValidator: UserValidator,
) {
    fun makeActivate(userId: String, userContent: UserContent) {
        userUpdater.updateActivate(userId, userContent)
    }

    fun updateUserImage(file: FileData, userId: String, category: FileCategory) {
        val media = fileProcessor.processNewFile(userId, file, category)
        val preMedia = userProcessor.processChangeImage(userId, media)
        fileProcessor.processOldFile(preMedia)
    }

    //사용자의 통합된 정보를 가져옴
    fun getFulledAccessUser(userId: String): Pair<User, UserStatus> {
        val user = userReader.read(userId)
        userValidator.isUserAccess(user)
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

    fun findOwnedEmoticonPacks(userId: String): List<EmoticonPack> {
        return userEmoticonFinder.find(userId)
    }
}