package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException.NotFound

@Component
class FriendRemover(
    private val friendShipRepository: FriendShipRepository
) {
    @Transactional
    fun removeFriend(userId: String, friendId: String) {
        friendShipRepository.remove(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
        friendShipRepository.remove(friendId, userId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }

    @Transactional
    fun blockFriend(userId: String, friendId: String) {
        friendShipRepository.block(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
        friendShipRepository.blocked(friendId, userId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
}