package org.chewing.v1.service

import org.chewing.v1.implementation.FriendSearchEngine
import org.chewing.v1.implementation.FriendSortEngine
import org.chewing.v1.model.Friend
import org.chewing.v1.model.FriendSearch
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class FriendSearchService(
    private val friendSearchEngine: FriendSearchEngine,
) {
    fun searchFriend(userId: User.UserId, keyword: String): List<Friend> {
        val friends = friendSearchEngine.searchFriends(userId, keyword)
        return FriendSortEngine.sortFriends(friends, SortCriteria.NAME)
    }

    fun addSearchFriendHistory(userId: User.UserId, keyword: FriendSearch) {
        friendSearchEngine.appendSearchHistory(userId, keyword)
    }

    fun getSearchFriendHistory(userId: User.UserId): List<FriendSearch> {
        val friendSearchHistory = friendSearchEngine.readFriendSearchHistory(userId)
        return FriendSortEngine.sortFriendSearchHistory(friendSearchHistory, SortCriteria.DATE)
    }
}