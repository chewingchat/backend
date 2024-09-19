package org.chewing.v1.model.comment

import java.time.LocalDateTime

class Comment(
    val id: CommentId,
    val comment: String,
    val createAt: LocalDateTime
) {
    companion object {
        fun of(
            commentId: String,
            comment: String,
            createAt: LocalDateTime
        ): Comment {
            return Comment(CommentId.of(commentId), comment, createAt)
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