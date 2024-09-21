package org.chewing.v1.implementation.comment

import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.feed.FeedUpdater
import org.chewing.v1.model.feed.FeedTarget
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentProcessor(
    private val feedReader: FeedReader,
    private val userReader: UserReader,
    private val commentRemover: CommentRemover,
    private val commentAppender: CommentAppender,
    private val commentReader: CommentReader,
    private val feedUpdater: FeedUpdater,
) {

    @Transactional
    fun processFeedComments(userId: String, feedId: String, comment: String, updateType: FeedTarget) {
        val feed = feedReader.readFeed(feedId)
        val user = userReader.read(userId)
        feedUpdater.updateFeed(feed.feedId, updateType)
        commentAppender.appendComment(user, comment, feed)
    }

    @Transactional
    fun processFeedUnComments(commentId: String, updateType: FeedTarget) {
        val comment = commentReader.read(commentId)
        feedUpdater.updateFeed(comment.feedId, updateType)
        commentRemover.removeComment(commentId)
    }
}