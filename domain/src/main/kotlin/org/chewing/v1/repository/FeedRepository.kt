package org.chewing.v1.repository

import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository {
    fun readFeedWithDetails(feedId: Feed.FeedId): Feed?
    fun readFeed(feedId: Feed.FeedId): Feed?
    fun checkFeedLike(feedId: Feed.FeedId, userId: User.UserId): Boolean
    fun readFeedsWithDetails(userId: User.UserId): List<Feed>
    fun readFeedWithWriter(feedId: Feed.FeedId): Feed?
    fun checkFeedsLike(feedIds: List<Feed.FeedId>, userId: User.UserId): Map<Feed.FeedId, Boolean>
    fun appendFeedComment(feed: Feed, comment: FeedComment)
    fun readFeedComment(feedId: Feed.FeedId): List<FeedComment>
    fun readFeedCommentsWithFeed(commentIds: List<FeedComment.CommentId>): List<Pair<FeedComment, Feed>>
    fun appendFeedLikes(feed: Feed, user: User)
    fun removeFeedLikes(feed: Feed, user: User)
    fun updateFeed(feed: Feed)
    fun removeFeed(feed: Feed)
    fun removeFeedComments(feed: Feed, commentId: FeedComment.CommentId)
    fun readUserCommentWithFeed(userId: User.UserId): List<Pair<Feed,FeedComment>>
}