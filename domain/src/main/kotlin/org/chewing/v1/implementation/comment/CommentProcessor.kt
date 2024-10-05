package org.chewing.v1.implementation.comment

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
    fun processComment(userId: String, feedId: String, comment: String, updateType: FeedTarget) {
        feedUpdater.update(feedId, updateType)
        commentAppender.appendComment(userId, comment, feedId)
    }

    @Transactional
    fun processUnComment(commentId: String, updateType: FeedTarget) {
        feedUpdater.update(commentId, updateType)
        commentRemover.remove(commentId)
    }
}