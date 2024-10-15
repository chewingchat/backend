package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.feed.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedUpdater(
    private val feedRepository: FeedRepository
) {
    fun update(feedId: String, target: FeedTarget) {
        feedRepository.update(feedId, target)
    }
}