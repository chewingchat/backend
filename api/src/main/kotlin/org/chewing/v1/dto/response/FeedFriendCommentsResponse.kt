package org.chewing.v1.dto.response

import org.chewing.v1.model.feed.FeedComment
import java.time.format.DateTimeFormatter

data class FeedFriendCommentsResponse(
    val comments: List<CommentFriendResponse>
) {
    companion object {
        fun of(
            comments: List<FeedComment>
        ): FeedFriendCommentsResponse {
            return FeedFriendCommentsResponse(
                comments = comments.map { CommentFriendResponse.of(it) }
            )
        }
    }
    data class CommentFriendResponse(
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
            ): CommentFriendResponse {
                return CommentFriendResponse(
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