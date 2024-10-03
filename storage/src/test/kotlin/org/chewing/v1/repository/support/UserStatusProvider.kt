package org.chewing.v1.repository.support

import org.chewing.v1.model.user.UserStatus
import org.springframework.stereotype.Component

@Component
object UserStatusProvider {

    fun buildSelected(userId: String): UserStatus {
        return UserStatus.of("statusId", "message", "emoji", userId, true)
    }
    fun buildNotSelected(userId: String): UserStatus {
        return UserStatus.of("statusId", "message", "emoji", userId, false)
    }
}