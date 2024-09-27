package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedAppender(
    private val feedRepository: FeedRepository
) {
    fun appendFeedLikes(feedInfo: FeedInfo, user: User) {
        feedRepository.likes(feedInfo, user)
    }
    fun appendFeed(medias: List<Media>, user: User, topic: String): String {
        return feedRepository.append(medias, user, topic)
    }
}