package org.chewing.v1.implementation.user

import org.chewing.v1.model.user.StatusInfo
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class StatusReader(
    private val userStatusRepository: UserStatusRepository
) {
    fun readSelectedStatuses(userIds: List<String>): List<StatusInfo> {
        return userStatusRepository.readSelectedUsersStatus(userIds)
    }
    fun readStatuses(userId: String): List<StatusInfo> {
        return userStatusRepository.readUserStatuses(userId)
    }
    fun readSelectedStatus(userId: String): StatusInfo {
        return userStatusRepository.readSelectedUserStatus(userId)
    }
}