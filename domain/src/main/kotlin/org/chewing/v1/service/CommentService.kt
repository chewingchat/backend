package org.chewing.v1.service

import org.chewing.v1.implementation.comment.CommentEnricher
import org.chewing.v1.implementation.comment.CommentLocker
import org.chewing.v1.implementation.comment.CommentReader
import org.chewing.v1.implementation.comment.CommentValidator
import org.chewing.v1.implementation.feed.FeedValidator
import org.chewing.v1.implementation.friendship.FriendShipReader
import org.chewing.v1.implementation.notification.NotificationProcessor
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentLocker: CommentLocker,
    private val feedValidator: FeedValidator,
    private val commentValidator: CommentValidator,
    private val userReader: UserReader,
    private val friendShipReader: FriendShipReader,
    private val commentEnricher: CommentEnricher,
    private val notificationProcessor: NotificationProcessor
) {
    fun remove(userId: String, commentIds: List<String>, target: FeedTarget) {
        commentValidator.isOwned(userId, commentIds)
        commentLocker.lockUnComments(commentIds, target)
    }

    fun comment(userId: String, feedId: String, comment: String, target: FeedTarget) {
        val user = userReader.read(userId)
        feedValidator.isNotOwned(feedId, userId)
        commentLocker.lockComment(userId, feedId, comment, target)
        notificationProcessor.processCommentNotification(user, feedId)
    }

    fun getUserCommented(userId: String): List<CommentInfo> {
        return commentReader.readCommented(userId)
    }

    fun fetchComment(userId: String, feedId: String): List<Comment> {
        feedValidator.isOwned(feedId, userId)
        val comments = commentReader.reads(feedId)
        val friends = friendShipReader.readsAccessIdIn(comments.map { it.userId }, userId)
        val users = userReader.reads(friends.map { it.friendId })
        return commentEnricher.enrich(comments, friends, users)
    }
}