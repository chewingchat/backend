package org.chewing.v1.implementation.comment

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentAppender(
    private val commentRepository: CommentRepository
) {
    fun appendComment(comment: FeedComment, feed: Feed) {
        commentRepository.appendComment(comment, feed)
    }
}