package org.chewing.v1.repository

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.comment.Comment
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository {
    fun isCommentsOwner(userId: User.UserId, commentIds: List<Comment.CommentId>): Boolean
    fun readCommentsWithUserId(feedId: Feed.FeedId): List<Pair<User.UserId, Comment>>
    fun removeComment(commentId: Comment.CommentId)
    fun appendComment(user: User, comment: String, feed: Feed)
    fun readCommentsWithFeedId(userId: User.UserId): List<Pair<Feed.FeedId, Comment>>
}