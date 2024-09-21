package org.chewing.v1.implementation.feed

import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedUpdater(
    private val feedRepository: FeedRepository
) {
    fun updateFeed(feedId: String, target: FeedTarget) {
        feedRepository.update(feedId, target)
    }
}