package org.chewing.v1.implementation.comment

import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentAppender(
    private val commentRepository: CommentRepository
) {
    fun appendComment(user: User, comment: String, feedInfo: FeedInfo) {
        commentRepository.appendComment(user,comment, feedInfo)
    }
}