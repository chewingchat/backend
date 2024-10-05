package org.chewing.v1.implementation.comment

import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepository: CommentRepository
) {
    fun reads(feedId: String): List<CommentInfo> {
        return commentRepository.reads(feedId)
    }

    fun readCommented(userId: String): List<CommentInfo> {
        return commentRepository.readsCommented(userId)
    }

    fun readsIn(commentIds: List<String>): List<CommentInfo> {
        return commentRepository.readsIn(commentIds)
    }
}