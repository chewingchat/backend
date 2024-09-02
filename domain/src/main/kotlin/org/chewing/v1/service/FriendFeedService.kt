package org.chewing.v1.service

import org.chewing.v1.implementation.feed.FeedChecker
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.feed.FeedSortEngine
import org.chewing.v1.model.Feed
import org.chewing.v1.model.FriendFeed
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class FriendFeedService(
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFeed(feedId)
        val sortedFeedDetails = FeedSortEngine.sortFeedDetails(feed.feedDetails, SortCriteria.INDEX)
        val updatedFeed = feed.updateFeedDetails(sortedFeedDetails)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(updatedFeed, isLiked)
    }
}