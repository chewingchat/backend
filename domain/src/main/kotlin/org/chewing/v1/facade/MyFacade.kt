package org.chewing.v1.facade

import org.chewing.v1.implementation.my.MyAggregator
import org.chewing.v1.model.comment.UserCommentedInfo
import org.chewing.v1.service.*
import org.springframework.stereotype.Service

@Service
class MyFacade(
    private val commentService: CommentService,
    private val feedService: FeedService,
    private val friendShipService: FriendShipService,
    private val myAggregator: MyAggregator,
    private val userService: UserService
) {
    fun getFeedUserCommented(userId: String): List<UserCommentedInfo> {
        val comments = commentService.getUserCommented(userId)
        val feeds = feedService.getFeeds(comments.map { it.feedId })
        val friendShips = friendShipService.getAccessFriendShipsIn(feeds.map { it.feed.userId }, userId)
        val users = userService.getUsers(friendShips.map { it.friendId })
        return myAggregator.aggregateUserCommented(friendShips, comments, users, feeds)
    }
}
