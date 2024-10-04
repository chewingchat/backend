package org.chewing.v1.implementation.friend

import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FriendRemover(
    private val friendShipRepository: FriendShipRepository
) {
    @Transactional
    fun removeFriend(userId: String, friendId: String) {
        friendShipRepository.remove(userId, friendId)
    }

    @Transactional
    fun blockFriend(userId: String, friendId: String) {
        friendShipRepository.block(userId, friendId)
    }
}