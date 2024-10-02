package org.chewing.v1.implementation.feed

import org.chewing.v1.repository.FeedLikesRepository
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedChecker(
    private val feedLikesRepository: FeedLikesRepository
) {
    fun checkLike(feedId: String, userId: String): Boolean {
        return feedLikesRepository.checkLike(feedId, userId)
    }
}