package org.chewing.v1.implementation.comment

import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.feed.FeedUpdater
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentProcessor(
    private val feedReader: FeedReader,
    private val userReader: UserReader,
    private val commentRemover: CommentRemover,
    private val commentAppender: CommentAppender,
    private val feedUpdater: FeedUpdater,
) {

    @Transactional
    fun processFeedComments(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        val feed = feedReader.readFeedWithDetails(feedId)
        val user = userReader.readUser(userId)
        val feedComment = FeedComment.generate(comment, user)
        commentAppender.appendComment(feedComment, feed)
        feedUpdater.updateFeed(feed.appendComments())
    }

    @Transactional
    fun processFeedUnComments(commentId: FeedComment.CommentId) {
        val feed = feedReader.readFulledFeedByCommentId(commentId)
        commentRemover.removeComment(commentId)
        feedUpdater.updateFeed(feed.removeComments())
    }
}