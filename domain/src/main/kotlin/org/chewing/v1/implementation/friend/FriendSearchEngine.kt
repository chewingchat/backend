package org.chewing.v1.implementation.friend

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Component

@Component
class FriendSearchEngine(
    private val friendReader: FriendReader,
    private val userReader: UserReader,
    private val friendEnricher: FriendEnricher,
) {
    fun search(userId: String, keyword: String): List<Friend> {
        val friendsInfo = friendReader.reads(userId)
        val users = userReader.reads(friendsInfo.map { it.friendId })
        val friendsStatus = userReader.readSelectedStatuses(friendsInfo.map { it.friendId })
        val enrichedFriends = friendEnricher.enriches(friendsInfo, users, friendsStatus)
        return personalized(enrichedFriends, cleanKeyword(keyword))
    }

    private fun cleanKeyword(keyword: String): String = keyword.replace(" ", "")

    private fun personalized(friends: List<Friend>, keyword: String): List<Friend> {
        return friends.filter { friend ->
            val fullName = "${friend.user.name.firstName()} ${friend.user.name.lastName()}"
            val alternativeFullName = "${friend.user.name.lastName()} ${friend.user.name.firstName()}"
            val concatenatedNames = "${friend.user.name.firstName()}${friend.user.name.lastName()}"

            listOf(fullName, alternativeFullName, concatenatedNames).any {
                it.contains(keyword, ignoreCase = true)
            }
        }
    }
}
