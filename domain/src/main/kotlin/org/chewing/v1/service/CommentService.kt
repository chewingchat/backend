package org.chewing.v1.service

import org.chewing.v1.implementation.comment.CommentLocker
import org.chewing.v1.implementation.comment.CommentReader
import org.chewing.v1.implementation.comment.CommentValidator
import org.chewing.v1.implementation.feed.FeedValidator
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentLocker: CommentLocker,
    private val feedValidator: FeedValidator,
    private val commentValidator: CommentValidator
) {
    fun deleteFeedComment(userId: User.UserId, commentIds: List<FeedComment.CommentId>, target: FeedTarget) {
        commentValidator.isCommentOwner(userId, commentIds)
        commentIds.forEach {
            commentLocker.lockFeedUnComments(it, target)
        }
    }

    fun addFeedComment(userId: User.UserId, feedId: Feed.FeedId, comment: String, target: FeedTarget) {
        feedValidator.isNotFeedOwner(feedId, userId)
        commentLocker.lockFeedComments(userId, feedId, comment, target)
    }

    fun getFeedUserCommented(userId: User.UserId): List<Pair<Feed, List<FeedComment>>> {
        val feedUserCommentedWithFeed = commentReader.readUserCommentsFulledFeeds(userId)
        return feedUserCommentedWithFeed
            .groupBy({ it.second }, { it.first })
            .map { (feed, comments) -> feed to comments }
    }

    fun getFeedComments(userId: User.UserId, feedId: Feed.FeedId): List<FeedComment> {
        feedValidator.isFeedOwner(feedId, userId)
        return commentReader.readFeedComments(feedId)
    }
}