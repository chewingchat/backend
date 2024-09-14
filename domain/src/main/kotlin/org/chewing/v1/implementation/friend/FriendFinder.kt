package org.chewing.v1.implementation.friend

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Component

@Component
class FriendFinder(
    private val friendReader: FriendReader,
    private val userReader: UserReader,
) {
    fun findFriendsWithStatus(userId: User.UserId): List<Friend> {
        // 친구 목록 조회
        val friends = friendReader.readFriends(userId)

        // 사용자 상태 목록 조회 및 Map으로 변환
        val friendsWithStatusMap = userReader.readUsersWithStatuses(friends.map { it.friend.userId })
            .associateBy { it.userId }

        // 친구 목록을 맵을 사용하여 최적화된 상태로 변환
        return friends.map { friend ->
            val friendWithStatus = friendsWithStatusMap[friend.friend.userId]
            // 친구 상태가 없는 경우에는 기본값을 제공
            friend.updateFriend(friendWithStatus!!)
        }
    }
}