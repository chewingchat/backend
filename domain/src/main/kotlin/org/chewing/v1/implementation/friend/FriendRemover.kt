package org.chewing.v1.implementation.friend

import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FriendRemover(
    private val friendRepository: FriendRepository
) {
    @Transactional
    fun removeFriend(userId: String, friendId: String) {
        friendRepository.removeFriend(userId, friendId)
    }
}