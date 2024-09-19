package org.chewing.v1.dto.response.comment

import org.chewing.v1.model.comment.FriendComment
import java.time.format.DateTimeFormatter

data class FeedCommentsResponse(
    val comments: List<FriendCommentResponse>
) {
    companion object {
        fun of(
            comments: List<FriendComment>
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
                friendComment: FriendComment
            ): FriendCommentResponse {
                return FriendCommentResponse(
                    friendId = friendComment.friend.friend.userId.value(),
                    friendFirstName = friendComment.friend.friend.name.firstName(),
                    friendLastName = friendComment.friend.friend.name.lastName(),
                    friendImageUrl = friendComment.friend.friend.image.url,
                    comment = friendComment.comment.comment,
                    commentTime = friendComment.comment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                )
            }
        }
    }
}