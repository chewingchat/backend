package org.chewing.v1.implementation

import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendRemover(
    private val friendRepository: FriendRepository
) {
    fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendRepository.removeFriend(userId, friendId)
    }
}