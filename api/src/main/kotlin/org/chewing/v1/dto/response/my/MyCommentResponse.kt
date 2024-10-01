package org.chewing.v1.dto.response.my

import org.chewing.v1.dto.response.feed.MainFeedResponse
import org.chewing.v1.dto.response.friend.FriendInfoResponse
import org.chewing.v1.dto.response.friend.FriendResponse
import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.comment.UserCommentedInfo
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend
import java.time.format.DateTimeFormatter

data class MyCommentResponse(
    val myComments: List<MyCommentFeedResponse>
) {
    companion object {
        fun of(feedWithComments: List<UserCommentedInfo>): MyCommentResponse {
            // Feed ID로 그룹화
            val feedMap = feedWithComments.groupBy { it.feed.feed.feedId }

            return MyCommentResponse(
                feedMap.map { (_, commentsPerFeed) ->
                    val firstEntry = commentsPerFeed.first()
                    val feed = firstEntry.feed
                    val friend = firstEntry.friend
                    val comments = commentsPerFeed.map { it.comment }
                    MyCommentFeedResponse.of(feed, friend, comments)
                }
            )
        }
    }

    data class MyCommentFeedResponse(
        val friend: FriendInfoResponse,
        val feed: MainFeedResponse,
        val comments: List<CommentResponse>
    ) {
        companion object {
            fun of(
                feed: Feed,
                friend: Friend,
                comments: List<CommentInfo>
            ): MyCommentFeedResponse {
                return MyCommentFeedResponse(
                    FriendInfoResponse.of(
                        friendId = friend.user.userId,
                        userName = friend.name,
                        imageUrl = friend.user.image.url,
                        imageType = friend.user.image.type.toString().lowercase(),
                        access = friend.user.status
                    ),
                    MainFeedResponse.of(feed),
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