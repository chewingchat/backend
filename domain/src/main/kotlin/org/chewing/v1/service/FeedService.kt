package org.chewing.v1.service

import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.feed.*
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.FriendFeed
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.FeedComment
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker,
    private val userReader: UserReader,
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFeedWithDetails(feedId)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(feed, isLiked)
    }

    fun getFeed(userId: User.UserId, feedId: Feed.FeedId): Feed {
        return feedReader.readFeedWithDetails(feedId)
    }

    fun addFeedComment(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        feedLocker.lockFeedComments(userId, feedId, comment)
    }

    fun getFeedComment(userId: User.UserId, feedId: Feed.FeedId): List<FeedComment> {
        val feed = feedReader.readFeedWithWriter(feedId)
        val user = userReader.readUser(userId)
        FeedValidator.isFeedOwner(feed, user)
        return feedReader.readFeedComment(feedId)
    }

    fun getCommentFeed(userId: User.UserId): List<Pair<Feed, List<FeedComment>>> {
        val feedCommentsWithFeed = feedReader.readUserCommentWithFeed(userId)
        return feedCommentsWithFeed
            .groupBy { it.first.feedId.value() }  // feedId를 기준으로 그룹화
            .map { (feedId, feedCommentPairs) ->
                val feed = feedCommentPairs.first().first  // 각 그룹의 첫 번째 피드
                val comments = feedCommentPairs.map { it.second }  // 해당 피드에 연결된 모든 댓글
                feed to comments
            }
    }

    fun deleteFeedComment(userId: User.UserId, commentIds: List<FeedComment.CommentId>) {
        val commentsWithFeed = feedReader.readFeedsCommentWithFeed(commentIds)
        val user = userReader.readUser(userId)
        commentsWithFeed.forEach { (comment, feed) ->
            FeedValidator.isCommentOwner(comment, user)
            feedLocker.lockFeedUnComments(feed.feedId, comment.commentId)
        }
    }

    fun addFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedChecker.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId)
    }

    fun deleteFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedChecker.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId)
    }

    fun deleteFeed(userId: User.UserId, feedId: Feed.FeedId) {
        val feed = feedReader.readFeedWithWriter(feedId)
        val user = userReader.readUser(userId)
        FeedValidator.isFeedOwner(feed, user)
        feedRemover.removeFeed(feed)
    }
}