package org.chewing.v1.implementation.feed

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedUpdater(
    private val feedRepository: FeedRepository
) {
    fun updateFeed(feed: Feed) {
        feedRepository.updateFeed(feed)
    }
}