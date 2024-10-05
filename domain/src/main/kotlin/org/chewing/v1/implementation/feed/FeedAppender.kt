package org.chewing.v1.implementation.feed

import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.FeedDetailRepository
import org.chewing.v1.repository.FeedLikesRepository
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedAppender(
    private val feedRepository: FeedRepository,
    private val feedLikesRepository: FeedLikesRepository,
    private val feedDetailRepository: FeedDetailRepository
) {
    fun appendLikes(feedId: String, userId: String) {
        feedLikesRepository.likes(feedId, userId)
    }

    fun appendFeed(medias: List<Media>, userId: String, topic: String): String {
        val feedId = feedRepository.append(userId, topic)
        feedDetailRepository.append(medias, feedId)
        return feedId
    }
}