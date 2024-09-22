package org.chewing.v1.dto.response.comment

import org.chewing.v1.model.comment.Comment
import java.time.format.DateTimeFormatter

data class FeedCommentsResponse(
    val comments: List<FriendCommentResponse>
) {
    companion object {
        fun of(
            comments: List<Comment>
        ): FeedCommentsResponse {
            return FeedCommentsResponse(
                comments = comments.map { FriendCommentResponse.of(it) }
            )
        }
    }
    data class FriendCommentResponse(
        val friendId: String,
        val friendFirstName: String,
        val friendLastName: String,
        val friendImageUrl: String,
        val comment: String,
        val commentTime: String
    ) {
        companion object {
            fun of(
                friendComment: Comment
            ): FriendCommentResponse {
                return FriendCommentResponse(
                    friendId = friendComment.writer.userId,
                    friendFirstName = friendComment.writer.name.firstName(),
                    friendLastName = friendComment.writer.name.lastName(),
                    friendImageUrl = friendComment.writer.image.url,
                    comment = friendComment.comment,
                    commentTime = friendComment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                )
            }
        }
    }
}