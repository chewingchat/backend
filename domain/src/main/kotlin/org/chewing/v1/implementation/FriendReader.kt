package org.chewing.v1.implementation

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.Friend
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class FriendReader(
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository
) {
    fun readFriends(userId: User.UserId): List<Friend> {
        return friendRepository.readFriends(userId)
    }

    fun readFriend(userId: User.UserId, friendId: User.UserId): Pair<User, Friend> {
        return friendRepository.readFriend(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }

    fun readFriendWithEmail(friendEmail: String): User {
        return userRepository.readUserByEmail(friendEmail) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }

    fun readFriendWithPhoneNumber(friendPhoneNumber: String): User {
        return userRepository.readUserByEmail(friendPhoneNumber) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
}