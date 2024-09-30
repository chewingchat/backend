package org.chewing.v1.implementation.feed

import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedChecker(
    private val feedRepository: FeedRepository
) {
    fun checkLike(feedId: String, userId: String): Boolean {
        return feedRepository.checkLike(feedId, userId)
    }
}