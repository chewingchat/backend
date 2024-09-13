package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedRemover(
    val feedRepository: FeedRepository
) {
    fun removeFeedLikes(feed: Feed, user: User) {
        feedRepository.removeFeedLikes(feed, user)
    }
    fun removeFeeds(feedIds: List<Feed.FeedId>) {
        feedRepository.removeFeeds(feedIds)
    }
}