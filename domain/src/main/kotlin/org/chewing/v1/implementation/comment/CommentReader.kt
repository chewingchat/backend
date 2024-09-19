package org.chewing.v1.implementation.comment

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.comment.Comment
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepository: CommentRepository
) {
    fun readCommentWithUserId(feedId: Feed.FeedId): List<Pair<User.UserId, Comment>> {
        return commentRepository.readCommentsWithUserId(feedId)
    }

    fun readUserCommentedFeed(userId: User.UserId): List<Pair<Feed.FeedId, Comment>> {
        return commentRepository.readCommentsWithFeedId(userId)
    }
}