package org.chewing.v1.implementation.feed

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.FeedRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedRemover(
    val feedRepository: FeedRepository
) {
    @Transactional
    fun removeFeedLikes(feedInfo: FeedInfo, user: User) {
        feedRepository.unlikes(feedInfo, user)
    }
    @Transactional
    fun removeFeeds(feedIds: List<String>): List<Media>{
        feedRepository.removes(feedIds)
        return feedRepository.removesDetails(feedIds)
    }
}