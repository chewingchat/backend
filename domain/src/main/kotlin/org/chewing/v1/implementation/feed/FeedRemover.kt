package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedRemover(
    val feedRepository: FeedRepository
) {
    @Transactional
    fun removeFeedLikes(feed: Feed, user: User) {
        feedRepository.removeFeedLikes(feed, user)
    }
    @Transactional
    fun removeFeeds(feedIds: List<Feed.FeedId>): List<Media>{
        feedRepository.removeFeeds(feedIds)
        return feedRepository.removeFeedsDetail(feedIds)
    }
}