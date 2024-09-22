package org.chewing.v1.implementation.facade

import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.model.UserStatus
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
        val (user, userStatus) = userService.fulledUser(userId)
        val friends = friendService.getFriends(userId, sort)
        return Triple(user, userStatus, friends)
    }
}