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
    fun appendFeedComment(feed: Feed, comment: FeedComment) {
        feedRepository.appendFeedComment(feed, comment)
    }
    fun appendFeedLikes(feed: Feed, user: User) {
        feedRepository.appendFeedLikes(feed, user)
    }

    fun updateFeed(feed: Feed) {
        feedRepository.updateFeed(feed)
    }
}