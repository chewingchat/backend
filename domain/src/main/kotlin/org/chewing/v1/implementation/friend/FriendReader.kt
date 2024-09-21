package org.chewing.v1.implementation.friend

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.friend.FriendInfo
import org.chewing.v1.repository.FriendRepository
import org.springframework.stereotype.Component

@Component
class FriendReader(
    private val friendRepository: FriendRepository,
) {
    fun reads(userId: String): List<FriendInfo> {
        return friendRepository.readFriends(userId)
    }

    fun readsIn(friendIds: List<String>, userId: String): List<FriendInfo> {
        return friendRepository.readFriendsByIds(friendIds, userId)
    }

    fun readFriend(userId: String, friendId: String): FriendInfo {
        return friendRepository.readFriend(userId, friendId) ?: throw NotFoundException(ErrorCode.FRIEND_NOT_FOUND)
    }
}