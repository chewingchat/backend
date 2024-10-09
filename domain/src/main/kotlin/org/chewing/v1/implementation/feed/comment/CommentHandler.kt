package org.chewing.v1.implementation.feed.comment

import org.chewing.v1.implementation.feed.feed.FeedUpdater
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentHandler(
    private val commentRemover: CommentRemover,
    private val commentAppender: CommentAppender,
    private val feedUpdater: FeedUpdater,
) {

    @Transactional
    fun handleComment(userId: String, feedId: String, comment: String, updateType: FeedTarget) {
        feedUpdater.update(feedId, updateType)
        commentAppender.appendComment(userId, comment, feedId)
    }

    //존재 하는 댓글에 대해서만 업데이트 진행
    @Transactional
    fun handleUnComment(commentId: String, updateType: FeedTarget) {
        commentRemover.remove(commentId)?.let {
            feedUpdater.update(it, updateType)
        }
    }
}