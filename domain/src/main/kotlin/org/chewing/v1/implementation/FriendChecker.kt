package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendChecker(
    private val friendRepository: FriendRepository
) {
    fun isAlreadyFriend(userId: User.UserId, friendId: User.UserId) {
         if(friendRepository.checkFriend(userId, friendId)) {
             throw ConflictException(ErrorCode.FRIEND_ALREADY_CREATED)
         }
    }
}