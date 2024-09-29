package org.chewing.v1.implementation.facade

import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.service.FriendService
import org.chewing.v1.service.UserService
import org.springframework.stereotype.Service

@Service
class MainFacade(
    private val userService: UserService,
    private val friendService: FriendService,
) {
    fun getMainPage(userId: String, sort: SortCriteria): Triple<User, UserStatus, List<Friend>> {
        val (user, userStatus) = userService.getFulledAccessUser(userId)
        val friends = friendService.getSortedFriends(userId, sort)
        return Triple(user, userStatus, friends)
    }
}