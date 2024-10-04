package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component

@Component
class FriendValidator(
    private val friendShipRepository: FriendShipRepository
) {
    fun validateAddAllowed(userId: String, friendId: String) {
        validateMyself(userId, friendId)
        friendShipRepository.read(userId, friendId)?.let {
            validateBlock(it)
            validateBlocked(it)
        }
    }

    private fun validateBlock(friendShip: FriendShip) {
        if (friendShip.type == AccessStatus.BLOCK) {
            throw ConflictException(ErrorCode.FRIEND_BLOCK)
        }
    }

    private fun validateBlocked(friendShip: FriendShip) {
        if (friendShip.type == AccessStatus.BLOCKED) {
            throw ConflictException(ErrorCode.FRIEND_BLOCKED)
        }
    }

    private fun validateAccess(friendShip: FriendShip) {
        if (friendShip.type != AccessStatus.ACCESS) {
            throw ConflictException(ErrorCode.FRIEND_NOT_FOUND)
        }
    }

    private fun validateMyself(userId: String, friendId: String) {
        if (userId == friendId) {
            throw ConflictException(ErrorCode.FRIEND_MYSELF)
        }
    }

    fun validateFriendShipAllowed(userId: String, friendId: String) {
        validateMyself(userId, friendId)
        friendShipRepository.read(userId, friendId)?.let {
            validateBlock(it)
            validateBlocked(it)
            validateAccess(it)
        }
    }
}