package org.chewing.v1.implementation.comment

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.chewing.v1.repository.CommentRepository
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepository: CommentRepository
) {
    fun readComments(commentIds: List<FeedComment.CommentId>): List<FeedComment> {
        return commentRepository.readComments(commentIds)
    }

    fun readUserCommentsFulledFeeds(userId: User.UserId): List<Pair<FeedComment, Feed>> {
        return commentRepository.readUserCommentsFulledFeeds(userId)
    }

    fun readFeedComments(feedId: Feed.FeedId): List<FeedComment> {
        return commentRepository.readFeedComments(feedId)
    }
}