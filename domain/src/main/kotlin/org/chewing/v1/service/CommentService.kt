package org.chewing.v1.service

import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.comment.CommentLocker
import org.chewing.v1.implementation.comment.CommentReader
import org.chewing.v1.implementation.comment.CommentValidator
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.feed.FeedValidator
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentReader: CommentReader,
    private val commentLocker: CommentLocker,
    private val feedValidator: FeedValidator,
    private val commentValidator: CommentValidator
) {
    fun deleteFeedComment(userId: User.UserId, commentIds: List<FeedComment.CommentId>) {
        commentValidator.isCommentOwner(userId, commentIds)
        commentIds.forEach {
            commentLocker.lockFeedUnComments(it)
        }
    }

    fun addFeedComment(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        feedValidator.isNotFeedOwner(feedId, userId)
        commentLocker.lockFeedComments(userId, feedId, comment)
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