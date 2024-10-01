package org.chewing.v1.implementation.facade

import org.chewing.v1.implementation.my.MyAggregator
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.comment.UserCommentedInfo
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.service.CommentService
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.FriendService
import org.springframework.stereotype.Service

@Service
class MyFacade(
    private val commentService: CommentService,
    private val feedService: FeedService,
    private val friendService: FriendService,
    private val myAggregator: MyAggregator
) {
    fun getFeedUserCommented(userId: String): List<UserCommentedInfo> {
        val comments = commentService.getUserCommented(userId)
        val feeds = feedService.getFeeds(comments.map { it.feedId })
        val friends = friendService.getFriends(feeds.map { it.feed.userId }, userId)
        return myAggregator.aggregateUserCommented(feeds, friends, comments)
    }
}
