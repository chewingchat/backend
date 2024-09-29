package org.chewing.v1.implementation.friend

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

    fun readsIdIn(friendIds: List<String>, userId: String): List<FriendInfo> {
        return friendRepository.readFriendsByIds(friendIds, userId)
    }
}