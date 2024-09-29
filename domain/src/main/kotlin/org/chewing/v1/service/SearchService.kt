package org.chewing.v1.service

import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.friend.FriendSearchEngine
import org.chewing.v1.implementation.friend.FriendSortEngine
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val friendSearchEngine: FriendSearchEngine,
    private val userAppender: UserAppender,
    private val userReader: UserReader
) {
    fun searchFriends(userId: String, keyword: String): List<Friend> {
        val friends = friendSearchEngine.search(userId, keyword)
        return FriendSortEngine.sort(friends, SortCriteria.NAME)
    }

    fun addSearchedFriend(userId: String, search: FriendSearch) {
        val user = userReader.read(userId)
        return userAppender.appendSearched(user, search)
    }
    fun getSearchedFriend(userId: String,sortCriteria: SortCriteria): List<FriendSearch> {
        val friendSearchHistory = userReader.readSearched(userId)
        return FriendSortEngine.sortFriendSearchedHistory(friendSearchHistory,sortCriteria)
    }
}