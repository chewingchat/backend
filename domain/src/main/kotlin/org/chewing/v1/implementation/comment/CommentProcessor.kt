package org.chewing.v1.implementation.comment

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.feed.FeedUpdater
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentProcessor(
    private val commentRemover: CommentRemover,
    private val commentAppender: CommentAppender,
    private val feedUpdater: FeedUpdater,
) {

    @Transactional
    fun processFeedComments(userId: String, feedId: String, comment: String, updateType: FeedTarget) {
        feedUpdater.updateFeed(feedId, updateType)
        commentAppender.appendComment(userId, comment, feedId)
    }

    @Transactional
    fun processFeedUnComments(commentId: String, updateType: FeedTarget) {
        feedUpdater.updateFeed(commentId, updateType)
        commentRemover.remove(commentId)
    }
}