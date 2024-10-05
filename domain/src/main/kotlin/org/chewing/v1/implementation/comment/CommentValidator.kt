package org.chewing.v1.implementation.comment

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentValidator(
    private val commentReader: CommentReader
) {
    fun isOwned(userId: String, commentIds: List<String>) {
        val comments = commentReader.readsIn(commentIds)
        if (comments.size != commentIds.size) {
            throw ConflictException(ErrorCode.COMMENT_NOT_FOUND)
        }
        if (comments.any { it.userId != userId }) {
            throw ConflictException(ErrorCode.COMMENT_IS_NOT_OWNED)
        }
    }
}