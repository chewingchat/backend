package org.chewing.v1.implementation.comment

import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CommentRemover(
    private val commentRepository: CommentRepository
) {
    fun removeComment(commentId: String) {
        commentRepository.removeComment(commentId)
    }

    @Transactional
    fun removeAll(feedIds: List<String>){
        commentRepository.removesByFeedId(feedIds)
    }
    @Transactional
    fun removeAllCommented(userId: String) {
        commentRepository.removeCommented(userId)
    }
}