package org.chewing.v1.implementation.feed

import org.chewing.v1.implementation.comment.CommentRemover
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedProcessor(
    private val feedRemover: FeedRemover,
    private val feedAppender: FeedAppender,
    private val feedUpdater: FeedUpdater,
    private val commentRemover: CommentRemover,
) {
    @Transactional
    fun processFeedLikes(feedId: String, userId: String, target: FeedTarget) {
        feedAppender.appendFeedLikes(feedId, userId)
        feedUpdater.updateFeed(feedId, target)
    }

    @Transactional
    fun processFeedUnLikes(feedId: String, userId: String, target: FeedTarget) {
        feedRemover.removeLikes(feedId, userId)
        feedUpdater.updateFeed(feedId, target)
    }

    @Transactional
    fun processNewFeed(medias: List<Media>, userId: String, topic: String) {
        feedAppender.appendFeed(medias, userId, topic)
    }

    @Transactional
    fun processFeedHides(feedId: String, target: FeedTarget) {
        feedUpdater.updateFeed(feedId, target)
    }

    @Transactional
    fun processFeedUnHides(feedId: String, target: FeedTarget) {
        feedUpdater.updateFeed(feedId, target)
    }

    @Transactional
    fun processFeedRemoves(feedIds: List<String>): List<Media> {
        val oldMedias = feedRemover.removes(feedIds)
        commentRemover.removes(feedIds)
        return oldMedias
    }
}

