package org.chewing.v1.implementation.feed

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedProcessor(
    private val feedReader: FeedReader,
    private val feedRemover: FeedRemover,
    private val feedAppender: FeedAppender,
    private val feedUpdater: FeedUpdater
) {
    @Transactional
    fun processFeedLikes(feedId: String, userId: String, target: FeedTarget) {
        val feed = feedReader.readFeed(feedId)
        feedAppender.appendFeedLikes(feed, userId)
        feedUpdater.updateFeed(feedId, target)
    }

    @Transactional
    fun processFeedUnLikes(feedId: String, userId: String, target: FeedTarget) {
        val feed = feedReader.readFeed(feedId)
        feedRemover.removeLikes(feed, userId)
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
}

