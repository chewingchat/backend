package org.chewing.v1.implementation.user

import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class UserStatusReader(
    private val userStatusRepository: UserStatusRepository
) {

    fun readsSelected(userIds: List<String>): List<UserStatus> {
        return userStatusRepository.readSelectedUsersStatus(userIds)
    }

    fun read(userId: String): UserStatus {
        return userStatusRepository.readSelectedUserStatus(userId)
    }

    fun reads(userId: String): List<UserStatus> {
        return userStatusRepository.readsUserStatus(userId)
    }
}