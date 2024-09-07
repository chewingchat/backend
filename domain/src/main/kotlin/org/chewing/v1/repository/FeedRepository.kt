package org.chewing.v1.repository

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun readFeedWithDetails(feedId: Feed.FeedId): Feed?
    fun readFeed(feedId: Feed.FeedId): Feed?
    fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun readFeedsWithDetails(userId: User.UserId): List<Feed>
    fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean>
    fun addFeedComment(feed: Feed,user: User, comment: String)
    fun appendFeedLikes(feed: Feed, user: User)
    fun removeFeedLikes(feed: Feed, user: User)
    fun updateFeed(feed: Feed)
}