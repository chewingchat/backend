package org.chewing.v1.dto.response.my

import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend
import java.time.format.DateTimeFormatter

data class MyCommentResponse(
    val feeds: List<MyCommentFeedResponse>
) {
    companion object {
        fun of(feedWithComments: List<Triple<Feed, Friend, CommentInfo>>): MyCommentResponse {
            // Feed ID로 그룹화
            val groupedByFeed = feedWithComments.groupBy { (feed, _) -> feed.feed.feedId }

            return MyCommentResponse(
                groupedByFeed.map { (feedId, feedComments) ->
                    val feed = feedComments.first().first
                    val friend = feedComments.first().second
                    val comments = feedComments.map { it.third }
                    MyCommentFeedResponse.of(feed, friend, comments)
                }
            )
        }
    }

    data class MyCommentFeedResponse(
        val friendId: String,
        val friendFirstName: String,
        val friendLastName: String,
        val friendImageUrl: String,
        val friendActivate: String,
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
                friend: Friend,
                comments: List<CommentInfo>
            ): MyCommentFeedResponse {
                return MyCommentFeedResponse(
                    friend.friend.userId,
                    friend.name.firstName(),
                    friend.name.lastName(),
                    friend.friend.image.url,
                    friend.friend.type.name,
                    feed.feed.feedId,
                    feed.feed.topic,
                    feed.feedDetails[0].media.url,
                    feed.feed.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")),
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
                    comment: CommentInfo
                ): CommentResponse {
                    return CommentResponse(
                        commentId = comment.commentId,
                        comment = comment.comment,
                        commentTime = comment.createAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
                    )
                }
            }
        }
    }
}