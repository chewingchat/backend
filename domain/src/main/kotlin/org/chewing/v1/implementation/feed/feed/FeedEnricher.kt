package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.feed.FriendFeed
import org.springframework.stereotype.Component

@Component
class
FeedEnricher {
    fun enrichFriendFeeds(
        feeds: List<FeedInfo>,
        likedFeedIds: List<String>,
        feedDetails: List<FeedDetail>
    ): List<FriendFeed> {
        val likedFeedIdMap = likedFeedIds.associateWith { true }
        val feedDetailMap = feedDetails.groupBy { it.feedId }

        return feeds.map { feedInfo ->
            val isLiked = likedFeedIdMap[feedInfo.feedId] ?: false
            val details = feedDetailMap[feedInfo.feedId] ?: emptyList()

            FriendFeed.of(
                feedInfo.feedId,
                feedInfo.topic,
                feedInfo.uploadAt,
                isLiked,
                feedInfo.likes,
                details,
            )
        }
    }

    fun enrichFeeds(
        feeds: List<FeedInfo>,
        feedDetails: List<FeedDetail>
    ): List<Feed> {
        val feedDetailMap = feedDetails.groupBy { it.feedId }
        return feeds.map { feedInfo ->
            val details = feedDetailMap[feedInfo.feedId] ?: emptyList()
            Feed.of(
                feedInfo,
                details,
            )
        }
    }
}
