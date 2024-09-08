package org.chewing.v1.model.feed

import org.chewing.v1.model.User
import java.time.LocalDateTime

class FeedComment(
    val commentId: CommentId,
    val comment: String,
    val writer: User,
    val createAt: LocalDateTime
) {
    companion object {
        fun of(
            commentId: String,
            comment: String,
            writer: User,
            createAt: LocalDateTime
        ): FeedComment {
            return FeedComment(CommentId.of(commentId), comment, writer, createAt)
        }
    }

    class CommentId private constructor(private val commentId: String) {
        fun value(): String {
            return commentId
        }

        companion object {
            fun of(value: String): CommentId {
                return CommentId(value)
            }
        }
    }
}