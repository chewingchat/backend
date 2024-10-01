package org.chewing.v1.implementation.my

import org.chewing.v1.model.comment.CommentInfo
import org.chewing.v1.model.comment.UserCommentedInfo
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Component

@Component
class MyAggregator {

    fun aggregateUserCommented(
        feeds: List<Feed>,
        friends: List<Friend>,
        comments: List<CommentInfo>
    ): List<UserCommentedInfo> {
        val friendsMap = friends.associateBy { it.user.userId }
        val feedMap = feeds.associateBy { it.feed.feedId }

        return comments.mapNotNull { comment ->
            val feed = feedMap[comment.feedId]
            val friend = feed?.let { friendsMap[it.feed.userId] }
            if (feed != null && friend != null) {
                UserCommentedInfo.of(comment, friend, feed)
            } else {
                null
            }
        }
    }
}