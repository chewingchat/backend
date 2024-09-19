package org.chewing.v1.implementation.comment

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentValidator(
    private val commentRepository: CommentRepository
) {
    fun isCommentOwner(userId: User.UserId, commentIds: List<Comment.CommentId>) {
        if(!commentRepository.isCommentsOwner(userId, commentIds)){
            throw ConflictException(ErrorCode.COMMENT_IS_NOT_OWNED)
        }
    }
}