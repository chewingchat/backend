package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class FriendReader(
    private val friendRepository: FriendRepository,
) {
    fun readFriendsWithStatus(userId: User.UserId): List<Friend> {
        return friendRepository.readFriendsWithStatus(userId)
    }

    fun readFriend(userId: User.UserId, friendId: User.UserId): Friend {
        return friendRepository.readFriend(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
    fun readFriendWithStatus(userId: User.UserId, friendId: User.UserId): Friend {
        return friendRepository.readFriendWithStatus(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
}