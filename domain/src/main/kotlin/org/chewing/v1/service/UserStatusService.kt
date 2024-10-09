package org.chewing.v1.service

import org.chewing.v1.implementation.user.status.UserStatusAppender
import org.chewing.v1.implementation.user.status.UserStatusReader
import org.chewing.v1.implementation.user.status.UserStatusRemover
import org.chewing.v1.implementation.user.status.UserStatusUpdater
import org.chewing.v1.model.user.UserStatus
import org.springframework.stereotype.Service

@Service
class UserStatusService(
    private val userStatusReader: UserStatusReader,
    private val userStatusUpdater: UserStatusUpdater,
    private val userStatusRemover: UserStatusRemover,
    private val userStatusAppender: UserStatusAppender

) {
    fun getUserStatuses(userId: String): List<UserStatus> {
        return userStatusReader.reads(userId)
    }

    fun getSelectedStatuses(userIds: List<String>): List<UserStatus> {
        return userStatusReader.readsSelected(userIds)
    }

    fun getUserStatus(userId: String): UserStatus {
        return userStatusReader.read(userId)
    }

    fun selectUserStatus(userId: String, statusId: String) {
        userStatusUpdater.updateSelectedTrue(userId, statusId)
    }

    fun changeSelectUserStatus(userId: String) {
        userStatusUpdater.updateDeselected(userId)
    }

    fun deleteUserStatuses(statusesId: List<String>) {
        userStatusRemover.removes(statusesId)
    }

    fun createUserStatus(userId: String, message: String, emoji: String) {
        userStatusAppender.append(userId, message, emoji)
    }
    fun deleteAllUserStatuses(userId: String) {
        userStatusRemover.removeUsers(userId)
    }
}