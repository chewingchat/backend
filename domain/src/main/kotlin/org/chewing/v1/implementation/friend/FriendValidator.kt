package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendValidator(
    private val friendRepository: FriendRepository
) {
    fun validateAlreadyFriend(userId: String, friendId: String) {
         if(friendRepository.checkFriend(userId, friendId)) {
             throw ConflictException(ErrorCode.FRIEND_ALREADY_CREATED)
         }
    }
    fun validateMyself(userId: String, friendId: String) {
        if(userId == friendId) {
            throw ConflictException(ErrorCode.FRIEND_MYSELF)
        }
    }
    fun validateIsFriend(userId: String, friendId: String) {
        if(!friendRepository.checkFriend(userId, friendId)) {
            throw ConflictException(ErrorCode.FRIEND_NOT_FOUND)
        }
    }
}