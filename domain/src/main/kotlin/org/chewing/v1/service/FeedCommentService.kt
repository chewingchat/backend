package org.chewing.v1.service

import org.chewing.v1.implementation.feed.comment.*
import org.chewing.v1.implementation.notification.NotificationHandler
import org.chewing.v1.implementation.user.user.UserReader
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Service

@Service
class FeedCommentService(
    private val commentReader: CommentReader,
    private val commentLocker: CommentLocker,
    private val commentRemover: CommentRemover,
    private val commentValidator: CommentValidator,
    private val notificationHandler: NotificationHandler
) {
    fun remove(userId: String, commentIds: List<String>, target: FeedTarget) {
        commentValidator.isOwned(userId, commentIds)
        commentLocker.lockUnComments(commentIds, target)
    }

    fun removes(feedIds: List<String>) {
        commentRemover.removes(feedIds)
    }

    fun comment(userId: String, feedId: String, comment: String, target: FeedTarget) {
        commentLocker.lockComment(userId, feedId, comment, target)
        notificationHandler.handleCommentNotification(userId, feedId)
    }

    fun getOwnedComment(userId: String): List<CommentInfo> {
        return commentReader.readsOwned(userId)
    }

    fun getComment(feedId: String): List<CommentInfo> {
        return commentReader.reads(feedId)
    }
}