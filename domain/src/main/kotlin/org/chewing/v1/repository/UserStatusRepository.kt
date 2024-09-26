package org.chewing.v1.repository

import org.chewing.v1.model.user.StatusInfo

interface UserStatusRepository {
    fun readUserStatuses(userId: String): List<StatusInfo>
    fun removeUserStatus(statusId: String)
    fun readSelectedUserStatus(userId: String): StatusInfo
    fun readSelectedUsersStatus(userIds: List<String>): List<StatusInfo>
    fun removeByUserId(userId: String)
}