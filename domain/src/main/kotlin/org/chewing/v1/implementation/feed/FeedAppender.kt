package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedAppender(
    private val feedRepository: FeedRepository
) {
    fun appendFeedLikes(feed: Feed, user: User) {
        feedRepository.appendFeedLikes(feed, user)
    }
    fun appendFeed(feed: Feed): Feed.FeedId {
        return feedRepository.appendFeed(feed)
    }
}