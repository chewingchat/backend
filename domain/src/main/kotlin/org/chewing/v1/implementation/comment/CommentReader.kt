package org.chewing.v1.implementation.comment

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepository: CommentRepository
) {
    fun reads(feedId: String): List<CommentInfo> {
        return commentRepository.readComment(feedId)
    }

    fun readCommented(userId: String): List<CommentInfo> {
        return commentRepository.readCommented(userId)
    }
    fun read(commentId: String): CommentInfo {
        return commentRepository.read(commentId)?: throw NotFoundException(ErrorCode.COMMENT_NOT_FOUND)
    }
}