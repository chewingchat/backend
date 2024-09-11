package org.chewing.v1.implementation.facade

import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.chewing.v1.service.UserService
import org.springframework.stereotype.Service

@Service
class MainFacade (
    private val userService: UserService,
    private val friendService: FriendService,
    private val feedService: FeedService
){
    fun getMainPage(userId: User.UserId, sort: SortCriteria): Pair<User, List<Friend>> {
        val user = userService.getUserInfo(userId)
        val friends = friendService.getFriends(userId, sort)
        return Pair(user, friends)
    }
}