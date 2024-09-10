package org.chewing.v1.service

import org.chewing.v1.implementation.UserAppender
import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.friend.FriendSearchEngine
import org.chewing.v1.implementation.friend.FriendSortEngine
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val friendSearchEngine: FriendSearchEngine,
    private val userAppender: UserAppender,
    private val userReader: UserReader
) {
    fun searchFriends(userId: User.UserId, keyword: String): List<Friend> {
        val friends = friendSearchEngine.searchFriends(userId, keyword)
        return FriendSortEngine.sortFriends(friends, SortCriteria.NAME)
    }

    fun addSearchedFriend(userId: User.UserId, search: FriendSearch) {
        val user = userReader.readUser(userId)
        return userAppender.appendSearchedFriend(user, search)
    }
    fun getSearchedFriend(userId: User.UserId): List<FriendSearch> {
        val friendSearchHistory = userReader.readSearchedFriend(userId)
        return FriendSortEngine.sortFriendSearchedHistory(friendSearchHistory, SortCriteria.DATE)
    }
}