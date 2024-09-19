package org.chewing.v1.implementation.comment

import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.friend.FriendReader
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.comment.FeedComment
import org.chewing.v1.model.comment.FriendComment
import org.chewing.v1.model.feed.Feed
import org.springframework.stereotype.Component

@Component
class CommentFinder(
    private val commentReader: CommentReader,
    private val friendReader: FriendReader
) {
    fun findFriendCommented(feedId: Feed.FeedId, userId: User.UserId): List<FriendComment> {
        val commentWithUserId = commentReader.readCommentWithUserId(feedId)
        val usersById = friendReader.readFriendsByIds(commentWithUserId.map { it.first }, userId)
            .associateBy { it.friend.userId }
        return commentWithUserId.mapNotNull { (userId, comment) ->
            usersById[userId]?.let { FriendComment.of(comment, it) }
        }
    }
}