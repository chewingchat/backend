package org.chewing.v1.implementation.feed

import org.chewing.v1.model.user.User
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
    fun removeLikes(feedInfo: FeedInfo, user: User) {
        feedRepository.unlikes(feedInfo, user)
    }

    @Transactional
    fun removes(feedIds: List<String>): List<Media> {
        feedRepository.removes(feedIds)
        return feedRepository.removesDetails(feedIds)
    }
}