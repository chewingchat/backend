package org.chewing.v1.dto.request

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.model.feed.FeedTarget

class CommentRequest(
) {
    data class AddCommentRequest(
        val feedId: String = "",
        val comment: String = ""
    ){
        fun toFeedId(): Feed.FeedId {
            return Feed.FeedId.of(feedId)
        }
        fun toComment(): String {
            return comment
        }
        fun toUpdateType(): FeedTarget {
            return FeedTarget.COMMENTS
        }
    }

    data class DeleteCommentRequest(
        val commentId: String = "",
    ){
        fun toCommentId(): FeedComment.CommentId {
            return FeedComment.CommentId.of(commentId)
        }
    }
}