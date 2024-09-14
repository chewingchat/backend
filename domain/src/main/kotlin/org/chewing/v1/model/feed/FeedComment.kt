package org.chewing.v1.model.feed

import org.chewing.v1.model.User
import java.time.LocalDateTime

class FeedComment(
    val id: CommentId,
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
        fun generate(
            comment: String,
            writer: User
        ): FeedComment {
            return FeedComment(CommentId.of(""), comment, writer, LocalDateTime.now())
        }
    }

    class CommentId private constructor(private val commentId: String) {
        fun value(): String {
            return commentId
        }

        fun empty(): CommentId {
            return CommentId("")
        }

        companion object {
            fun of(value: String): CommentId {
                return CommentId(value)
            }
        }
    }
}