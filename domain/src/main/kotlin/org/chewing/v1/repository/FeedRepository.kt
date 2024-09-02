package org.chewing.v1.repository

import org.chewing.v1.model.Feed
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun readFeed(feedId: Feed.FeedId): Feed?
    fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun readUserFeed(userId: User.UserId): List<Feed>
    fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean>
}