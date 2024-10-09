package org.chewing.v1.facade

import org.chewing.v1.implementation.feed.comment.FeedAggregator
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedStatus
import org.chewing.v1.service.*
import org.springframework.stereotype.Component

@Component
class FeedFacade(
    private val feedService: FeedService,
    private val feedCommentService: FeedCommentService,
    private val feedLikeService: FeedLikesService,
    private val friendShipService: FriendShipService,
    private val userService: UserService,
    private val feedAggregator: FeedAggregator
) {
    fun removesFeed(userId: String, feedIds: List<String>) {
        feedService.removes(userId, feedIds)
        feedCommentService.removes(feedIds)
        feedLikeService.unlikes(feedIds)
    }

    fun getOwnedFeed(userId: String, feedId: String, type: FeedStatus): Pair<Feed, Boolean> {
        val feed = feedService.getOwnedFeed(feedId, type)
        val isLiked = feedLikeService.checkLike(feedId, userId)
        return Pair(feed, isLiked)
    }

    fun fetches(userId: String, feedId: String): List<Comment> {
        val comments = feedCommentService.getComment(feedId)
        val friendShips = friendShipService.getAccessFriendShipsIn(comments.map { it.userId }, userId)
        val users = userService.getUsers(friendShips.map { it.friendId })
        return feedAggregator.aggregates(comments, friendShips, users)
    }
}