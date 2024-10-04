package org.chewing.v1.repository

import org.chewing.v1.model.user.User
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.comment.CommentInfo
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository {
    fun isCommentsOwner(userId: String, commentIds: List<String>): Boolean
    fun readComment(feedId: String): List<CommentInfo>
    fun appendComment(userId: String, feedId: String, comment: String)
    fun readCommented(userId: String): List<CommentInfo>
    fun remove(commentId: String)
    fun removes(feedIds: List<String>)
    fun read(commentId: String): CommentInfo?
}