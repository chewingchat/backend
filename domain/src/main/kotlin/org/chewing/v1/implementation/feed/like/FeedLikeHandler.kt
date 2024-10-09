package org.chewing.v1.implementation.feed.like

import org.chewing.v1.implementation.feed.feed.FeedUpdater
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedLikeHandler(
    private val feedLikeAppender: FeedLikeAppender,
    private val feedLikeRemover: FeedLikeRemover,
    private val feedUpdater: FeedUpdater
) {

    @Transactional
    fun handleFeedLikes(feedId: String, userId: String, target: FeedTarget) {
        feedLikeAppender.appendLikes(feedId, userId)
        feedUpdater.update(feedId, target)
    }

    @Transactional
    fun handleFeedUnLikes(feedId: String, userId: String, target: FeedTarget) {
        feedLikeRemover.removeLikes(feedId, userId)
        feedUpdater.update(feedId, target)
    }
}