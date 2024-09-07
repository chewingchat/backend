package org.chewing.v1.service

import org.chewing.v1.implementation.UserReader
import org.chewing.v1.implementation.feed.*
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.friend.FriendFeed
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker,
    private val userReader: UserReader,
    private val feedAppender: FeedAppender,
    private val feedLocker: FeedLocker
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFeedWithDetails(feedId)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(feed, isLiked)
    }

    fun addFeedComment(userId: User.UserId, feedId: Feed.FeedId, comment: String) {
        val feed = feedReader.readFeed(feedId)
        val user = userReader.readUser(userId)
        feedAppender.appendFeedComment(feed, user, comment)
    }

    fun addFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedChecker.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId)
    }

    fun deleteFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedChecker.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId)
    }
}