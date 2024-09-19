package org.chewing.v1.implementation.feed

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FulledFeed
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.feed.FriendFeed
import org.springframework.stereotype.Component

@Component
class FeedEnricher {
    fun enrichFriendFeeds(
        friend: Friend,
        feeds: List<FulledFeed>,
        likedFeedIds: Map<Feed.FeedId, Boolean>
    ): List<FriendFeed> {
        return feeds.map { feed ->
            val isLiked = likedFeedIds[feed.feed.id] ?: false
            FriendFeed.of(feed, isLiked, friend)
        }
    }

    fun enrichFriendFeed(
        friendsFeed: List<Pair<FulledFeed, Friend>>,
        likedFeedIds: Map<Feed.FeedId, Boolean>
    ): List<FriendFeed> {
        return friendsFeed.map { (feed, friend) ->
            val isLiked = likedFeedIds[feed.feed.id] ?: false
            FriendFeed.of(feed, isLiked, friend)
        }
    }
}