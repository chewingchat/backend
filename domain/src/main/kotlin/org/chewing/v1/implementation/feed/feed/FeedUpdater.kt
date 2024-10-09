package org.chewing.v1.implementation.feed.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedUpdater(
    private val feedRepository: FeedRepository
) {
    @Transactional
    fun update(feedId: String, target: FeedTarget) {
        feedRepository.update(feedId, target) ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
}