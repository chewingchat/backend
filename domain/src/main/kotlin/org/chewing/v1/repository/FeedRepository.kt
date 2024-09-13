package org.chewing.v1.repository

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun readFulledFeed(feedId: Feed.FeedId): Feed?
    fun readFeed(feedId: Feed.FeedId): Feed?
    fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun readFulledFeedsByUserId(userId: User.UserId): List<Feed>
    fun readFulledFeeds(feedIds: List<Feed.FeedId>): List<Feed>
    fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean>
    fun appendFeedLikes(feed: Feed, user: User)
    fun removeFeedLikes(feed: Feed, user: User)
    fun updateFeed(feed: Feed)
    fun removeFeeds(feedIds: List<Feed.FeedId>)
    fun readFulledFeedByCommentId(commentId: FeedComment.CommentId): Feed
    fun appendFeed(feed: Feed): Feed.FeedId
}