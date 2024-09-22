package org.chewing.v1.repository

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.comment.CommentInfo
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository {
    fun isCommentsOwner(userId: String, commentIds: List<String>): Boolean
    fun readComment(feedId: String): List<CommentInfo>
    fun appendComment(user: User, comment: String, feedInfo: FeedInfo)
    fun readCommented(userId: String): List<CommentInfo>
    fun removeComment(commentId: String)
    fun removeCommented(userId: String)
    fun removesByFeedId(feedIds: List<String>)
    fun read(commentId: String): CommentInfo?
}