package org.chewing.v1.implementation.friend

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendInfo
import org.springframework.stereotype.Component

@Component
class FriendEnricher {
    fun enriches(friendsInfos: List<FriendInfo>, users: List<User>, userStatuses: List<UserStatus>): List<Friend> {
        val userMap = users.associateBy { it.userId }
        val statusMap = userStatuses.associateBy { it.userId }

        return friendsInfos.mapNotNull { friendInfo ->
            val user = userMap[friendInfo.friendId]
            val status = statusMap[friendInfo.friendId]
            status?.let {
                Friend.of(user!!, friendInfo.isFavorite, user.name, it)
            }
        }
    }
}