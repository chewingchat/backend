package org.chewing.v1.implementation.feed

import org.chewing.v1.implementation.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedProcessor(
    private val feedReader: FeedReader,
    private val userReader: UserReader,
    private val feedRemover: FeedRemover,
    private val feedAppender: FeedAppender,
) {
    @Transactional
    fun processFeedLikes(feedId: Feed.FeedId, userId: User.UserId) {
        val feed = feedReader.readFeedWithDetails(feedId)
        val user = userReader.readUser(userId)
        feedAppender.appendFeedLikes(feed.appendLikes(), user)
    }

    @Transactional
    fun processFeedUnLikes(feedId: Feed.FeedId, userId: User.UserId) {
        val feed = feedReader.readFeedWithDetails(feedId)
        val user = userReader.readUser(userId)
        feedRemover.removeFeedLikes(feed.removeLikes(), user)
    }
}

