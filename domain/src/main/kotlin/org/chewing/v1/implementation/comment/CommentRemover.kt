package org.chewing.v1.implementation.comment

import org.chewing.v1.model.comment.Comment
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentRemover(
    private val commentRepository: CommentRepository
) {
    fun removeComment(commentId: Comment.CommentId) {
        commentRepository.removeComment(commentId)
    }
}