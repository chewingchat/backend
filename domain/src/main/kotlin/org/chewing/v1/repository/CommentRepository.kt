package org.chewing.v1.repository

import org.chewing.v1.model.User
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository {
    fun readComments(commentIds: List<FeedComment.CommentId>): List<FeedComment>
    fun removeComment(commentId: FeedComment.CommentId)
    fun appendComment(comment: FeedComment, feed: Feed)
    fun readFeedComments(feedId: Feed.FeedId): List<FeedComment>
    fun readUserCommentsFulledFeeds(userId: User.UserId): List<Pair<FeedComment, Feed>>
}