package org.chewing.v1.implementation.facade

import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.service.CommentService
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.chewing.v1.service.UserService
import org.springframework.stereotype.Service

//wrapper class
@Service
class MyFacade(
    private val userService: UserService,
    private val friendService: FriendService,
    private val feedService: FeedService,
    private val commentService: CommentService
) {
    fun getMyFriends(userId: User.UserId, sort: SortCriteria): Pair<User, List<Friend>> {
        val friends = friendService.getFriends(userId, sort)
        val user = userService.getUserInfo(userId)
        return Pair(user, friends)
    }
}