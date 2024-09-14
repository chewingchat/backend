package org.chewing.v1.implementation

import org.chewing.v1.model.User
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class UserStatusReader(
    private val userStatusRepository: UserStatusRepository
) {
    fun readUserStatuses(userId: User.UserId): List<User.UserStatus> {
        return userStatusRepository.readUserStatuses(userId)
    }

    fun removeUserStatus(statusId: String) {
        userStatusRepository.removeUserStatus(statusId)
    }
}