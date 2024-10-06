package org.chewing.v1.service

import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.implementation.user.*
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.media.FileCategory
import org.chewing.v1.model.media.FileData
import org.chewing.v1.model.user.*
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
    private val fileProcessor: FileProcessor,
    private val userUpdater: UserUpdater,
    private val userValidator: UserValidator,
    private val userRemover: UserRemover,
    private val userAppender: UserAppender,
) {
    fun getUserProfile(userId: String): UserProfile {
        return userReader.readProfile(userId)
    }

    fun createUser(
        contact: Contact,
        appToken: String,
        device: PushToken.Device,
    ): User {
        val user = userAppender.appendIfNotExist(contact)
        userRemover.removePushToken(device)
        userAppender.appendUserPushToken(user, appToken, device)
        return user
    }

    fun makeAccess(userId: String, userContent: UserContent) {
        userUpdater.updateAccess(userId, userContent)
    }

    fun updateFile(file: FileData, userId: String, category: FileCategory) {
        val media = fileProcessor.processNewFile(userId, file, category)
        val oldMedia = userUpdater.updateFileUrl(userId, media)
        fileProcessor.processOldFile(oldMedia)
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

    fun updateUserContact(userId: String, contact: Contact) {
        userUpdater.updateContact(userId, contact)
    }

    fun deleteUser(userId: String) {
        val user = userReader.read(userId)
        userRemover.remove(userId)
        userRemover.removeUserStatuses(userId)
        fileProcessor.processOldFiles(listOf(user.image, user.backgroundImage))
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

    fun createUserStatus(userId: String, message: String, emoji: String) {
        userAppender.appendStatus(userId, message, emoji)
    }

    fun getUserStatuses(userId: String): List<UserStatus> {
        return userReader.readsUserStatus(userId)
    }
}