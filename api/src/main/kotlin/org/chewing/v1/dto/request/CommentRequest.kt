package org.chewing.v1.dto.request

import org.chewing.v1.model.feed.FeedComment

class CommentRequest(
) {
    data class AddCommentRequest(
        val feedId: String = "",
        val comment: String = ""
    )

    data class DeleteCommentRequest(
        val commentId: String = "",
    ){
        fun toCommentId(): FeedComment.CommentId {
            return FeedComment.CommentId.of(commentId)
        }
    }
}