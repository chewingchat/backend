package org.chewing.v1.implementation.friend

import org.chewing.v1.implementation.UserAppender
import org.chewing.v1.implementation.UserReader
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.springframework.stereotype.Component

@Component
class FriendSearchEngine(
    private val friendReader: FriendReader,
) {
    fun searchFriends(userId: User.UserId, keyword: String): List<Friend> {
        val friends = friendReader.readFriendsWithStatus(userId)
        return filterFriendsByKeyword(friends, cleanKeyword(keyword))
    }

    private fun cleanKeyword(keyword: String): String {
        return keyword.replace(" ", "") // 공백 제거
    }

    private fun filterFriendsByKeyword(friends: List<Friend>, keyword: String): List<Friend> {
        return friends.filter { friend ->
            // 성과 이름을 두 가지 순서로 조합
            val firstNameLastName = "${friend.friendName.firstName()} ${friend.friendName.lastName()}"
            val lastNameFirstName = "${friend.friendName.lastName()} ${friend.friendName.firstName()}"

            // 두 가지 조합과 검색 키워드를 비교
            firstNameLastName.contains(keyword, ignoreCase = true) ||
                    lastNameFirstName.contains(keyword, ignoreCase = true)
        }
    }
}