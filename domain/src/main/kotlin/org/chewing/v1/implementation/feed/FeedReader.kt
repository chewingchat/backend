package org.chewing.v1.implementation.feed

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.Feed
import org.chewing.v1.model.User
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component

@Component
class FeedReader(
    private val feedRepository: FeedRepository,
) {
    fun readFeed(feedId: Feed.FeedId): Feed {
        val feed = feedRepository.readFeed(feedId)
        return feed ?: throw NotFoundException(ErrorCode.FEED_NOT_FOUND)
    }
    fun readUserFeed(userId: User.UserId): List<Feed> {
        return feedRepository.readUserFeed(userId)
    }
}