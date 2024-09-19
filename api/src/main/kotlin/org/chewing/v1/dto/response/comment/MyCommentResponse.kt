package org.chewing.v1.dto.response.comment

import org.chewing.v1.model.comment.Comment
import org.chewing.v1.model.feed.FriendFeed
import java.time.format.DateTimeFormatter

data class MyCommentResponse(
    val feeds: List<MyCommentFeedResponse>
) {
    companion object {
        fun of(feedWithComments: List<Pair<FriendFeed, Comment>>): MyCommentResponse {
            // Feed ID로 그룹화
            val groupedByFeed = feedWithComments.groupBy { (feed, _) -> feed.fulledFeed.feed.id }

            return MyCommentResponse(
                groupedByFeed.map { (feedId, feedComments) ->
                    val feed = feedComments.first().first
                    val comments = feedComments.map { it.second }

                    MyCommentFeedResponse.of(feed, comments)
                }
            )
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
                friendFeed: FriendFeed,
                comments: List<Comment>
            ): MyCommentFeedResponse {
                return MyCommentFeedResponse(
                    friendFeed.friend.friend.userId.value(),
                    friendFeed.friend.friend.name.firstName(),
                    friendFeed.friend.friend.name.lastName(),
                    friendFeed.friend.friend.image.url,
                    friendFeed.fulledFeed.feed.id.value(),
                    friendFeed.fulledFeed.feed.topic,
                    friendFeed.fulledFeed.details[0].media.url,
                    friendFeed.fulledFeed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")),
                    friendFeed.fulledFeed.details[0].media.type.toString().lowercase(),
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
                    comment: Comment
                ): CommentResponse {
                    return CommentResponse(
                        commentId = comment.id.value(),
                        comment = comment.comment,
                        commentTime = comment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                    )
                }
            }
        }
    }
}