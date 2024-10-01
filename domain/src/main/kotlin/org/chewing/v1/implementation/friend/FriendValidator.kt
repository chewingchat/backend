package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.friend.FriendInfo
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendValidator(
    private val friendRepository: FriendRepository
) {
    fun validateFriendshipAllowed(userId: String, friendId: String) {
        validateMyself(userId, friendId)
        friendRepository.readFriendShip(userId, friendId)?.let {
            validateBlock(it.first)
            validateBlocked(it.second)
            throw ConflictException(ErrorCode.FRIEND_ALREADY_CREATED)
        }
    }

    private fun validateBlock(toTarget: FriendInfo) {
        if (toTarget.type == AccessStatus.BLOCK) {
            throw ConflictException(ErrorCode.FRIEND_BLOCK)
        }
    }

    private fun validateBlocked(fromTarget: FriendInfo) {
        if (fromTarget.type == AccessStatus.BLOCK) {
            throw ConflictException(ErrorCode.FRIEND_BLOCKED)
        }
    }

    private fun validateMyself(userId: String, friendId: String) {
        if (userId == friendId) {
            throw ConflictException(ErrorCode.FRIEND_MYSELF)
        }
    }

    fun validateIsFriend(userId: String, friendId: String) {
        if (!friendRepository.checkFriend(userId, friendId)) {
            throw ConflictException(ErrorCode.FRIEND_NOT_FOUND)
        }
    }
}