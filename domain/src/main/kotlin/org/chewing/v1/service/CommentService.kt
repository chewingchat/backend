package org.chewing.v1.service

import org.chewing.v1.implementation.comment.CommentFinder
import org.chewing.v1.implementation.comment.CommentLocker
import org.chewing.v1.implementation.comment.CommentReader
import org.chewing.v1.implementation.comment.CommentValidator
import org.chewing.v1.implementation.feed.FeedValidator
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.comment.FriendComment
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentLocker: CommentLocker,
    private val feedValidator: FeedValidator,
    private val commentValidator: CommentValidator,
    private val commentFinder: CommentFinder
) {
    fun deleteFeedComment(userId: User.UserId, commentIds: List<Comment.CommentId>, target: FeedTarget) {
        commentValidator.isCommentOwner(userId, commentIds)
        commentIds.forEach {
            commentLocker.lockFeedUnComments(it, target)
        }
    }

    fun addFeedComment(userId: User.UserId, feedId: Feed.FeedId, comment: String, target: FeedTarget) {
        feedValidator.isNotFeedOwner(feedId, userId)
        commentLocker.lockFeedComments(userId, feedId, comment, target)
    }

    fun getUserCommented(userId: User.UserId): List<Pair<Feed.FeedId, Comment>> {
        return commentReader.readUserCommentedFeed(userId)
    }

    fun getFriendCommented(userId: User.UserId, feedId: Feed.FeedId): List<FriendComment> {
        feedValidator.isFeedOwner(feedId, userId)
        return commentFinder.findFriendCommented(feedId, userId)
    }
}