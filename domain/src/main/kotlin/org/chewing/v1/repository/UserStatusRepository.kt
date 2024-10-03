package org.chewing.v1.repository

import org.chewing.v1.model.user.UserStatus

interface UserStatusRepository {
    fun removes(statusesId: List<String>)
    fun readSelectedUserStatus(userId: String): UserStatus
    fun readSelectedUsersStatus(userIds: List<String>): List<UserStatus>
    fun removeAllByUserId(userId: String)
    fun updateSelectedStatusTrue(userId: String, statusId: String)
    fun updateSelectedStatusFalse(userId: String)
    fun append(userId: String, statusMessage: String, emoji: String)
    fun readsUserStatus(userId: String): List<UserStatus>
}