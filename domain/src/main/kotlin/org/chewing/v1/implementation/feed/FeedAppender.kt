package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedAppender(
    private val feedRepository: FeedRepository
) {
    fun addFeedComment(feed: Feed, user: User, comment: String) {
        feedRepository.addFeedComment(feed, user, comment)
    }

    fun addFeedLikes(feed: Feed, user: User) {
        feedRepository.addFeedLikes(feed, user)
    }
    fun deleteFeedLikes(feed: Feed, user: User) {
        feedRepository.deleteFeedLikes(feed, user)
    }
}