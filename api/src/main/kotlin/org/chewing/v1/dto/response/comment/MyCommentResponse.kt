package org.chewing.v1.dto.response.comment

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import java.time.format.DateTimeFormatter

data class MyCommentResponse(
    val feeds: List<MyCommentFeedResponse>
) {
    companion object {
        fun of(feedCommentsWithFeed: List<Pair<Feed, List<FeedComment>>>): MyCommentResponse {
            return MyCommentResponse(
                feedCommentsWithFeed.map { (feed, comments) ->
                    MyCommentFeedResponse.of(feed, comments)
                })
        }
    }

    data class MyCommentFeedResponse(
        val friendId: String,
        val friendFirstName: String,
        val friendLastName: String,
        val friendImageUrl: String,
        val feedId: String,
        val feedTopic: String,
        val feedMainDetailFileUrl: String,
        val feedUploadTime: String,
        val type: String,
        val comments: List<CommentResponse>
    ) {
        companion object {
            fun of(
                feed: Feed,
                comments: List<FeedComment>
            ): MyCommentFeedResponse {
                return MyCommentFeedResponse(
                    feed.writer.userId.value(),
                    feed.writer.name.firstName(),
                    feed.writer.name.lastName(),
                    feed.writer.image.url,
                    feed.feedId.value(),
                    feed.feedTopic,
                    feed.feedDetails[0].media.url,
                    feed.feedUploadTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")),
                    feed.feedDetails[0].media.type.toString().lowercase(),
                    comments.map { CommentResponse.of(it) }
                )
            }
        }

        data class CommentResponse(
            val commentId: String,
            val comment: String,
            val commentTime: String
        ) {
            companion object {
                fun of(
                    comment: FeedComment
                ): CommentResponse {
                    return CommentResponse(
                        commentId = comment.commentId.value(),
                        comment = comment.comment,
                        commentTime = comment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                    )
                }
            }
        }
    }
}