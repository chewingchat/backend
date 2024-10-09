package org.chewing.v1.implementation.feed.comment

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentRemover(
    private val commentRepository: CommentRepository
) {
    fun remove(commentId: String): String? {
        return commentRepository.remove(commentId)
    }

    @Transactional
    fun removes(feedIds: List<String>) {
        commentRepository.removes(feedIds)
    }
}