package org.chewing.v1.facade

import org.chewing.v1.implementation.friend.FriendSearchEngine
import org.chewing.v1.implementation.search.SearchAggregator
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendSortCriteria
import org.chewing.v1.service.FriendShipService
import org.chewing.v1.service.UserService
import org.chewing.v1.service.UserStatusService
import org.springframework.stereotype.Service

@Service
class SearchFacade(
    private val friendShipService: FriendShipService,
    private val userService: UserService,
    private val userStatusService: UserStatusService,
    private val friendSearchEngine: FriendSearchEngine,
    private val searchAggregator: SearchAggregator
) {
    fun searchFriends(userId: String, keyword: String): List<Friend> {
        val friendShips = friendShipService.getAccessFriendShips(userId, FriendSortCriteria.NAME)
        val searchedFriendShips = friendSearchEngine.search(friendShips, keyword)
        val user = userService.getUsers(searchedFriendShips.map { it.friendId })
        val userStatus = userStatusService.getSelectedStatuses(friendShips.map { it.friendId })
        return searchAggregator.aggregates(searchedFriendShips, user, userStatus)
    }
}