package org.chewing.v1.implementation.feed

import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedAppender(
    private val feedRepository: FeedRepository
) {
    fun appendFeedLikes(feedInfo: FeedInfo, userId: String) {
        feedRepository.likes(feedInfo, userId)
    }
    fun appendFeed(medias: List<Media>, userId: String, topic: String): String {
        return feedRepository.append(medias, userId, topic)
    }
}