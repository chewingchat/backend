package org.chewing.v1.implementation.comment

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Component

@Component
class CommentValidator(
    private val commentReader: CommentReader,
    private val userReader: UserReader
) {
    fun isCommentOwner(userId: User.UserId, commentIds: List<FeedComment.CommentId>) {
        val user = userReader.readUser(userId)
        val comments = commentReader.readComments(commentIds)
        comments.forEach {
            if (it.writer.userId != user.userId) {
                throw ConflictException(ErrorCode.COMMENT_IS_NOT_OWNED)
            }
        }
    }
}