package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedAppender(
    private val feedRepository: FeedRepository
) {
    fun appendFeedComment(feed: Feed, user: User, comment: String) {
        feedRepository.addFeedComment(feed, user, comment)
    }
    fun appendFeedLikes(feed: Feed, user: User) {
        feedRepository.appendFeedLikes(feed, user)
    }

    fun updateFeed(feed: Feed) {
        feedRepository.updateFeed(feed)
    }
}