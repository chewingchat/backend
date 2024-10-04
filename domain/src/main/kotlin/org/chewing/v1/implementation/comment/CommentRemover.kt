package org.chewing.v1.implementation.comment

import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentRemover(
    private val commentRepository: CommentRepository
) {
    fun remove(commentId: String) {
        commentRepository.remove(commentId)
    }

    fun removes(feedIds: List<String>) {
        commentRepository.removes(feedIds)
    }
}