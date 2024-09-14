package org.chewing.v1.repository

import org.chewing.v1.model.User

interface UserStatusRepository {
    fun readUserStatuses(userId: User.UserId): List<User.UserStatus>
    fun removeUserStatus(statusId: String)
}