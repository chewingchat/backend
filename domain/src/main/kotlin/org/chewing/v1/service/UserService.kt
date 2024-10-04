package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedProcessor
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.user.*
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userProcessor: UserProcessor,
    private val userUpdater: UserUpdater,
    private val feedReader: FeedReader,
    private val userValidator: UserValidator,
    private val userRemover: UserRemover,
    private val userAppender: UserAppender,
    private val feedProcessor: FeedProcessor
) {
    fun getUserProfile(userId: String): UserProfile {
        return userReader.readProfile(userId)
    }

    fun makeAccess(userId: String, userContent: UserContent) {
        userUpdater.updateAccess(userId, userContent)
    }

    fun updateProfileImage(file: FileData, userId: String, category: FileCategory) {
        val media = fileProcessor.processNewFile(userId, file, category)
        val preMedia = userProcessor.processChangeImage(userId, media, category)
        fileProcessor.processOldFile(preMedia)
    }


    //사용자의 통합된 정보를 가져옴
    fun getFulledAccessUser(userId: String): Pair<User, UserStatus> {
        val user = userReader.read(userId)
        userValidator.isUserAccess(user)
        val userStatus = userReader.readSelectedStatus(userId)
        return Pair(user, userStatus)
    }

    fun updateName(userId: String, userName: UserName) {
        userUpdater.updateName(userId, userName)
    }

    fun updateBirth(userId: String, birth: String) {
        userUpdater.updateBirth(userId, birth)
    }

    fun deleteUser(userId: String) {
        val user = userReader.read(userId)
        userProcessor.processRemoveUser(userId)
        val feedIds = feedReader.readsOwnedInfo(userId, FeedStatus.ALL).map { it.feedId }
        val oldFiles = feedProcessor.processFeedRemoves(feedIds)
        val allFiles = sequenceOf(oldFiles, listOfNotNull(user.image, user.backgroundImage))
            .flatMap { it.asSequence() }
            .toList()
        fileProcessor.processOldFiles(allFiles)
    }

    fun selectUserStatus(userId: String, statusId: String) {
        userUpdater.updateSelectedStatusTrue(userId, statusId)
    }

    fun deselectUserStatus(userId: String) {
        userUpdater.updateDeselectedStatusFalse(userId)
    }

    fun deleteUserStatuses(statusesId: List<String>) {
        userRemover.removeStatuses(statusesId)
    }

    fun addUserStatus(userId: String, message: String, emoji: String) {
        userAppender.appendStatus(userId, message, emoji)
    }

    fun getUserStatuses(userId: String): List<UserStatus> {
        return userReader.readsUserStatus(userId)
    }

    fun changeTTS(userId: String, fileData: FileData) {
        val media = fileProcessor.processNewFile(userId, fileData, FileCategory.TTS)
        val oldMedia = userUpdater.updateTTS(userId, media)
        fileProcessor.processOldFile(oldMedia)
    }
}