package org.chewing.v1.implementation

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendReader(
    private val friendRepository: FriendRepository
) {

    fun readFriends(userId: User.UserId): Pair<List<Friend>, SortCriteria> {
        return friendRepository.readFriends(userId)
    }

    fun readFriend(userId: User.UserId, friendId: User.UserId): Pair<User, Friend> {
        return friendRepository.readFriend(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
}