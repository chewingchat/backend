package org.chewing.v1.implementation.comment

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentAppender(
    private val commentRepository: CommentRepository
) {
    fun appendComment(user: User, comment: String, feed: Feed) {
        commentRepository.appendComment(user,comment, feed)
    }
}