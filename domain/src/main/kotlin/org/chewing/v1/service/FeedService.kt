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
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover,
    private val feedValidator: FeedValidator
) {
    fun getFriendFeed(userId: User.UserId, feedId: Feed.FeedId): FriendFeed {
        val feed = feedReader.readFeedWithDetails(feedId)
        val isLiked = feedChecker.checkFeedLike(feedId, userId)
        return FriendFeed.of(feed, isLiked)
    }

    fun getFeed(userId: User.UserId, feedId: Feed.FeedId): Feed {
        return feedReader.readFeedWithDetails(feedId)
    }

    fun getFriendFeedsFull(userId: User.UserId): List<FriendFeed> {
        val feeds = feedReader.readFeedsWithDetails(userId)
        val likedFeedIds = feedChecker.checkFeedsLike(feeds.map { it.feedId }, userId)
        return feeds.map { feed ->
            val isLiked = likedFeedIds[feed.feedId] ?: false
            FriendFeed.of(feed, isLiked)
        }
    }

    fun addFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedValidator.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId)
    }

    fun deleteFeedLikes(userId: User.UserId, feedId: Feed.FeedId) {
        feedValidator.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId)
    }

    fun deleteFeed(userId: User.UserId, feedId: Feed.FeedId) {
        feedValidator.isFeedOwner(feedId, userId)
        feedRemover.removeFeed(feedId)
    }
}