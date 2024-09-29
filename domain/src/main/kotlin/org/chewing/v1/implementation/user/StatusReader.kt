package org.chewing.v1.implementation.user

import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class StatusReader(
    private val userStatusRepository: UserStatusRepository
) {
    fun readSelectedStatuses(userIds: List<String>): List<UserStatus> {
        return userStatusRepository.readSelectedUsersStatus(userIds)
    }
    fun readSelectedStatus(userId: String): UserStatus {
        return userStatusRepository.readSelectedUserStatus(userId)
    }
}