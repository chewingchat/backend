package org.chewing.v1.implementation.user

import org.chewing.v1.repository.UserStatusRepository
import org.springframework.stereotype.Component

@Component
class UserStatusAppender(
    private val userStatusRepository: UserStatusRepository
) {
    fun append(userId: String, statusMessage: String, emoji: String) {
        userStatusRepository.append(userId, statusMessage, emoji)
    }
}