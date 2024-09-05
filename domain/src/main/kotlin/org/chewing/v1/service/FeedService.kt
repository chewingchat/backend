package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedChecker
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.FriendFeed
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFeedWithDetails(feedId)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(feed, isLiked)
    }
}