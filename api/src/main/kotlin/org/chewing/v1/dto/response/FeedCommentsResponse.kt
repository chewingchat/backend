package org.chewing.v1.dto.response

import org.chewing.v1.model.feed.FeedComment
import java.time.format.DateTimeFormatter

data class FeedCommentsResponse(
    val comments: List<CommentResponse>
) {
    companion object {
        fun of(
            comments: List<FeedComment>
        ): FeedCommentsResponse {
            return FeedCommentsResponse(
                comments = comments.map { CommentResponse.of(it) }
            )
        }
    }
    data class CommentResponse(
        val friendId: String,
        val friendFirstName: String,
        val friendLastName: String,
        val friendImageUrl: String,
        val comment: String,
        val commentTime: String
    ) {
        companion object {
            fun of(
                feedComment: FeedComment
            ): CommentResponse {
                return CommentResponse(
                    friendId = feedComment.writer.userId.value(),
                    friendFirstName = feedComment.writer.name.firstName(),
                    friendLastName = feedComment.writer.name.lastName(),
                    friendImageUrl = feedComment.writer.image.url,
                    comment = feedComment.comment,
                    commentTime = feedComment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                )
            }
        }
    }
}